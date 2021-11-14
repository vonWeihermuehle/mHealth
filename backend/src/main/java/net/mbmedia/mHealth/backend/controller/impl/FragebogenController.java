package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.controller.IFragebogenController;
import net.mbmedia.mHealth.backend.fragebogen.IFragebogenService;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenAbgeschlossenEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenZuweisungEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.TO.FragebogenAbgeschlossenTO;
import net.mbmedia.mHealth.backend.fragebogen.impl.TO.FragebogenTO;
import net.mbmedia.mHealth.backend.mail.IMailService;
import net.mbmedia.mHealth.backend.user.ITherapeutPatientService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;
import static net.mbmedia.mHealth.backend.util.FailureAnswer.SOME;
import static net.mbmedia.mHealth.backend.util.FailureAnswer.failureAnswer;
import static net.mbmedia.mHealth.backend.util.RejectUtils.*;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.simpleSuccesAnswer;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.successAnswerWithObject;

@RestController
@RequestMapping(path = "/api/fragebogen")
public class FragebogenController extends BaseController implements IFragebogenController
{

    @Autowired
    private IFragebogenService fragebogenService;

    @Autowired
    private IMailService mailService;

    @Autowired
    private ITherapeutPatientService therapeutPatientService;

    @PostMapping("/addFragebogen")
    @Override
    public String addFragebogen(String token, String titel, String beschreibung, String json)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        FragebogenEntity fragebogenEntity = new FragebogenEntity.BUILDER()
                .withAuthor(userID.get())
                .withTitel(titel)
                .withBeschreibung(beschreibung)
                .withJson(json)
                .build();

        Optional<Long> id = fragebogenService.addFragebogen(fragebogenEntity);
        rejectIfNotPresent(id);

        return simpleSuccesAnswer();
    }

    @PostMapping("/updateFragebogen")
    @Override
    public String updateFragebogen(String token, Long id, String titel, String beschreibung, String json)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        Optional<FragebogenEntity> byId = fragebogenService.getById(id);
        rejectIfNotPresent(byId);
        rejectIfNot(byId.get().getAuthor().equals(userID.get()));

        FragebogenEntity updated = byId.get().toBuilder()
                .withTitel(titel)
                .withBeschreibung(beschreibung)
                .withJson(json)
                .build();

        fragebogenService.updateFragebogen(updated);
        return simpleSuccesAnswer();
    }

    @PostMapping("/delFragebogen")
    @Override
    public String delFragebogen(String token, Long id)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        Optional<FragebogenEntity> byId = fragebogenService.getById(id);
        rejectIfNotPresent(byId);
        rejectIfNot(byId.get().getAuthor().equals(userID.get()));

        fragebogenService.delFragebogen(id);
        return simpleSuccesAnswer();
    }

    @GetMapping("/getOwn")
    @Override
    public String getOwn(String token)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        boolean isTherapeut = isTherapeut(userID);

        List<FragebogenEntity> all = isTherapeut
                ? fragebogenService.getForAuthor(userID.get())
                : fragebogenService.getForEmpfaenger(userID.get());

        List<FragebogenTO> ausgabe = all.stream().map(FragebogenTO::mappe).collect(Collectors.toList());

        return successAnswerWithObject(ausgabe);
    }

    @PostMapping("/getZuweisungenFuerFragebogen")
    @Override
    public String getZuweisungenFuerFragebogen(String token, Long id)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        Optional<FragebogenEntity> byId = fragebogenService.getById(id);
        rejectIf(!byId.isPresent() || !byId.get().getAuthor().equals(userID.get()));

        List<FragebogenZuweisungEntity> allZuweisungenForFragebogenID = fragebogenService.getAllZuweisungenForFragebogenID(id);
        List<FragebogenZuweisungEntity> ausgabe = allZuweisungenForFragebogenID.stream().map(FragebogenZuweisungEntity::removeData).collect(Collectors.toList());

        return successAnswerWithObject(ausgabe);
    }

    @PostMapping("/addZuweisung")
    @Override
    public String addZuweisung(String token, Long id, String empfaengerID, String cron)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        Optional<FragebogenEntity> byId = fragebogenService.getById(id);
        Optional<UserEntity> empfaenger = userService.getById(empfaengerID);
        rejectIf(!byId.isPresent() || !byId.get().getAuthor().equals(userID.get()) || !empfaenger.isPresent());

        FragebogenZuweisungEntity zuweisung = new FragebogenZuweisungEntity.BUILDER()
                .withFragebogen(byId.get())
                .withEmpfaenger(empfaenger.get())
                .withCron(cron)
                .withErstellt(now())
                .build();
        Optional<Long> zuweisungID = fragebogenService.addZuweisung(zuweisung);

        return zuweisungID.isPresent()
                ? simpleSuccesAnswer()
                : failureAnswer(SOME);
    }

    @PostMapping("/delZuweisung")
    @Override
    public String delZuweisung(String token, Long id)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));

        Optional<FragebogenZuweisungEntity> zuweisungById = fragebogenService.getZuweisungById(id);
        rejectIf(!zuweisungById.isPresent() || !zuweisungById.get().getFragebogen().getAuthor().equals(userID.get()));

        fragebogenService.delZuweisung(id);
        return simpleSuccesAnswer();
    }

    @PostMapping("/addAbgeschlossen")
    @Override
    public String addAbgeschlossen(String token, String ergebnis, Long id)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isPatient(userID));

        Optional<UserEntity> patient = userService.getById(userID.get());
        Optional<FragebogenEntity> fragebogen = fragebogenService.getById(id);
        rejectIfNotPresent(patient, fragebogen);


        int wert = berechneWert(ergebnis);
        boolean schwellwertUeberschritten = patient.get().getSchwellwert() < wert;

        if (schwellwertUeberschritten)
        {
            Optional<UserEntity> therapeutFor = therapeutPatientService.getTherapeutFor(userID.get());
            rejectIfNotPresent(therapeutFor);
            mailService.sendSchwellWertUeberschritten(patient.get(), therapeutFor.get(), wert);
        }

        FragebogenAbgeschlossenEntity abgeschlossenEntity = new FragebogenAbgeschlossenEntity.BUILDER()
                .withTitel(fragebogen.get().getTitel())
                .withBeschreibung(fragebogen.get().getBeschreibung())
                .withPatient(patient.get())
                .withErgebnis(ergebnis)
                .withWert(wert)
                .withErstellt(now())
                .build();

        Optional<Long> optionalId = fragebogenService.addAbgeschlossen(abgeschlossenEntity);
        fragebogenService.delNonCronZuweisung(patient.get().getUuid(), id);

        return optionalId.isPresent()
                ? simpleSuccesAnswer()
                : failureAnswer(SOME);
    }

    @PostMapping("/getAbgeschlossenFuer")
    @Override
    public String getAbgeschlossenFuer(String token, String uuid)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !isTherapeut(userID));
        Optional<UserEntity> patient = userService.getById(uuid);
        rejectIfNotPresent(patient);

        List<FragebogenAbgeschlossenTO> abgeschlossenPojos = fragebogenService.getAllAbgeschlossenFuerPatient(uuid)
                .stream()
                .map(FragebogenAbgeschlossenTO::mappe)
                .collect(Collectors.toList());
        return successAnswerWithObject(abgeschlossenPojos);
    }

    /**
     * Möglicher Input trotz mehrerer Abschnitte
     * {"freieTextNachricht":"asdfasdf","ratingNummeroUno":0,"frageInNeuemAbschnitt":"asdf","ratingInNeuemAbschnitt":1}
     **/
    private int berechneWert(String ergebnis)
    {
        String ergebnisAngepasst = ergebnis.replace("}", ",");
        //TODO: regex passt nicht
        Pattern pattern = Pattern.compile("\\:([0-9]*?)\\,");
        Matcher matcher = pattern.matcher(ergebnisAngepasst);

        List<String> matched = new ArrayList<>();
        int i = 0;
        while (matcher.find())
        {
            matched.add(matcher.group(i));
            i++;
        }

        matched.stream()
                .map(s -> s.replace(",", "").replace(":", ""))
                .map(Integer::valueOf)
                .forEach(this::aggregiere);

        return aggregiert;
    }

    private int aggregiert = 0;

    private void aggregiere(int number)
    {
        this.aggregiert += number;
    }

}
