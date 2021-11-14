package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.chat.IChatService;
import net.mbmedia.mHealth.backend.chat.impl.TO.ChatTO;
import net.mbmedia.mHealth.backend.controller.IUserController;
import net.mbmedia.mHealth.backend.kontakt.IKontakteService;
import net.mbmedia.mHealth.backend.mail.IMailService;
import net.mbmedia.mHealth.backend.ort.IOrteService;
import net.mbmedia.mHealth.backend.unterstuetzung.IUnterstuetzungService;
import net.mbmedia.mHealth.backend.user.ITherapeutPatientService;
import net.mbmedia.mHealth.backend.user.impl.TherapeutPatientEntity;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import net.mbmedia.mHealth.backend.util.FailureAnswer;
import net.mbmedia.mHealth.backend.util.StandortHelper;
import net.mbmedia.mHealth.backend.util.UUIDHelper;
import org.eclipse.sisu.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static net.mbmedia.mHealth.backend.token.TokenGenerator.generateToken;
import static net.mbmedia.mHealth.backend.util.CryptoHelper.generateInitialPassword;
import static net.mbmedia.mHealth.backend.util.CryptoHelper.hash;
import static net.mbmedia.mHealth.backend.util.FailureAnswer.*;
import static net.mbmedia.mHealth.backend.util.RejectUtils.rejectIf;
import static net.mbmedia.mHealth.backend.util.RejectUtils.rejectIfNot;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.*;

@RestController
@RequestMapping(path = "/api/user")
public class UserController extends BaseController implements IUserController
{
    @Autowired
    private ITherapeutPatientService therapeutPatientService;

    @Autowired
    private IChatService chatService;

    @Autowired
    private IKontakteService kontakteService;

    @Autowired
    private IOrteService orteService;

    @Autowired
    private IUnterstuetzungService unterstuetzungService;

    @Autowired
    private IMailService mailService;

    @PostMapping("/login")
    @Override
    public String login(String username, String passwort)
    {
        List<UserEntity> entities = userService.login(username, hash(passwort));
        if (entities.size() == 1)
        {
            String token = generateToken(entities.get(0).getUuid());
            tokenService.addToken(token);
            return successAnswerWithString(token);
        }
        return failureAnswer(LOGIN_FAILED);
    }

    @Override
    public String resetPassword(String host, String email)
    {
        Optional<UserEntity> byEmail = userService.getByEmail(email);
        rejectIfNot(byEmail.isPresent());

        String newPasswort = generateInitialPassword(10);

        boolean erfolg = userService.updatePassword(byEmail.get().getUuid(), hash(newPasswort));
        if (erfolg)
        {
            mailService.sendNewPasswort(host, byEmail.get(), newPasswort);
            return simpleSuccesAnswer();
        }
        return failureAnswer(SOME);
    }

    @GetMapping("/getUserData")
    @Override
    public String getUserData(String token)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        Optional<UserEntity> user = userService.getById(userID.get());
        if (user.isPresent())
        {
            return successAnswerWithObject(user.get().removePasswort());
        }

        return failureAnswer(NO_PERMISSION);
    }

    @PostMapping("/addPatient")
    @Override
    public String addPatient(String host, String token, String vorname, String nachname, String email, String username, Integer schwellwert)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        if (!userService.isEmailAndUsernameUnused(username, email))
        {
            return failureAnswer(ALREADY_USED);
        }

        String initialPassword = generateInitialPassword(10);
        UserEntity patient = new UserEntity.BUILDER()
                .withUUID(UUIDHelper.generateUUID())
                .withVorname(vorname)
                .withNachname(nachname)
                .withEmail(email)
                .withTherapeut(false)
                .withPasswort(hash(initialPassword))
                .withUsername(username)
                .withSchwellwert(schwellwert)
                .build();

        userService.register(patient);

        TherapeutPatientEntity therapeutPatient = new TherapeutPatientEntity.BUILDER()
                .withPatient(patient.getUuid())
                .withTherapeut(userID.get())
                .build();

        therapeutPatientService.add(therapeutPatient);

        List<UserEntity> patientenFuer = therapeutPatientService.getPatientenFuer(userID.get());
        List<String> list = patientenFuer.stream().map(UserEntity::getUsername).collect(toList());
        if (list.contains(username))
        {
            mailService.sendRegisterConfirmation(host, patient, initialPassword);
            return simpleSuccesAnswer();
        }
        return failureAnswer(FailureAnswer.REGISTER_FAILED);
    }

    @PostMapping("/addTherapeut")
    @Override
    public String addTherapeut(String host, String token, String vorname, String nachname, String email, String username)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTechUser(token));

        if (!userService.isEmailAndUsernameUnused(username, email))
        {
            return failureAnswer(ALREADY_USED);
        }

        String initialPassword = generateInitialPassword(10);
        UserEntity therapeut = new UserEntity.BUILDER()
                .withUUID(UUIDHelper.generateUUID())
                .withVorname(vorname)
                .withNachname(nachname)
                .withEmail(email)
                .withTherapeut(true)
                .withPasswort(hash(initialPassword))
                .withUsername(username)
                .build();

        Optional<String> register = userService.register(therapeut);

        if (register.isPresent())
        {
            mailService.sendRegisterConfirmation(host, therapeut, initialPassword);
            return simpleSuccesAnswer();
        }

        return failureAnswer(FailureAnswer.REGISTER_FAILED);
    }

    @GetMapping("/showPatienten")
    @Override
    public String showPatienten(String token)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        List<UserEntity> patientenFuer = therapeutPatientService.getPatientenFuer(userID.get());
        List<UserEntity> list = patientenFuer.stream()
                .map(UserEntity::removePasswort)
                .collect(toList());

        return successAnswerWithObject(list);
    }

    @GetMapping("/showPatientenNearby")
    @Override
    public String showPatientenNearby(String token)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isPatient(userID));

        Optional<UserEntity> user = userService.getById(userID.get());
        StandortHelper.UmkreisPunkte umkreisPunkte = StandortHelper.berechne(user.get());

        List<UserEntity> patienten = userService.getNearby(umkreisPunkte);
        List<ChatTO> collect = patienten.stream()
                .filter(p -> !p.getUuid().equals(userID.get()))
                .map(p -> new ChatTO(p.getFullName(), p.getUsername(), p.getUuid(), false))
                .collect(toList());

        return successAnswerWithObject(collect);
    }

    @PostMapping("/setLastCoordinates")
    @Override
    public String setLastCoordinates(String token, @Nullable Double lat, @Nullable Double lng)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isPatient(userID));

        if (lat >= 999 || lng >= 999)
        {
            lat = null;
            lng = null;
        }

        userService.setLastCoordinates(userID.get(), lat, lng);
        return simpleSuccesAnswer();
    }

    @PostMapping("/setSchwellwert")
    @Override
    public String setSchwellwert(String token, String userID, Integer wert)
    {
        Optional<String> therapeutID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !therapeutID.isPresent() || !isTherapeut(therapeutID));

        Optional<UserEntity> byId = userService.getById(userID);
        rejectIfNot(byId.isPresent());

        userService.setSchwellwert(byId.get().getUuid(), wert);
        return simpleSuccesAnswer();
    }

    @PostMapping("/removeOwnUserWithAllData")
    @Override
    public String removeOwnUserWithAllData(String token)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isPatient(userID));

        userService.deleteUserById(userID.get());
        chatService.deleteAllMessagesFrom(userID.get());
        kontakteService.deleteKontakteFor(userID.get());
        orteService.deleteFor(userID.get());
        therapeutPatientService.deleteForPatient(userID.get());
        unterstuetzungService.deleteForPatient(userID.get());

        return simpleSuccesAnswer();
    }

    @PostMapping("/changePassword")
    @Override
    public String changePassword(String token, String altesPasswort, String neuesPasswort)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        Optional<UserEntity> user = userService.getById(userID.get());
        if (!user.get().getPasswort().equals(hash(altesPasswort)))
        {
            return failureAnswer(SOME);
        }

        if (userService.updatePassword(userID.get(), hash(neuesPasswort)))
        {
            return simpleSuccesAnswer();
        } else
        {
            return failureAnswer(SOME);
        }
    }


}
