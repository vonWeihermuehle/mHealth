package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.controller.IUserController;
import net.mbmedia.mHealth.backend.mail.impl.EmailParameterTestDatenErzeuger;
import net.mbmedia.mHealth.backend.param.IParameterService;
import net.mbmedia.mHealth.backend.param.impl.ParameterEntity;
import net.mbmedia.mHealth.backend.token.TokenService;
import net.mbmedia.mHealth.backend.user.ITherapeutPatientService;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.TherapeutPatientEntity;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.token.TokenGenerator.generateToken;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.*;
import static net.mbmedia.mHealth.backend.util.CryptoHelper.hash;
import static net.mbmedia.mHealth.backend.util.FailureAnswer.LOGIN_FAILED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest
{
    @Autowired
    private IUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IUserController userController;

    @Autowired
    private ITherapeutPatientService therapeutPatientService;

    @Autowired
    private IParameterService parameterService;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(userService, tokenService, therapeutPatientService, parameterService);
    }

    @Test
    public void login_soll_bei_korrekten_Daten_token_liefern()
    {
        UserEntity userEntity = getStandardUserEntity();
        userService.register(userEntity);

        String response = userController.login(userEntity.getUsername(), STANDARD_PASSWORT);

        System.out.println(response);
        assert (response).contains("string");
    }

    @Test
    public void login_soll_bei_falschen_Daten_Fehler_ausgeben()
    {
        UserEntity userEntity = getStandardUserEntity();
        userService.register(userEntity);

        String response = userController.login(userEntity.getUsername(), "falsches Passwort");

        assert (response).contains(LOGIN_FAILED.getMessage());
    }

    @Test
    public void bei_login_soll_token_valide_gesetzt_werden()
    {
        UserEntity userEntity = getStandardUserEntity();
        userService.register(userEntity);

        String response = userController.login(userEntity.getUsername(), STANDARD_PASSWORT);

        boolean tokenExpired = tokenService.isTokenExpired(getTokenFromResponse(response));
        assert (!tokenExpired);
    }

    @Test
    public void getUserData()
    {
        UserEntity userEntity = getStandardUserEntity();
        userService.register(userEntity);

        String token = generateTokenAndValidate(userEntity);

        String response = userController.getUserData(token);

        assert (response).contains(userEntity.getUuid());
        assert (!response.contains(userEntity.getPasswort()));
    }

    @Test
    public void addPatient()
    {
        persistEmailParam();
        UserEntity therapeut = getStandardUserEntityBuilder().withTherapeut(true).build();
        userService.register(therapeut);

        String token = generateTokenAndValidate(therapeut);

        String username = "username";
        String response = userController.addPatient("localhost", token, "vorname", "nachname", "email@test.de", username, 10);

        assert (response).contains("success");
        List<UserEntity> patientenFuer = therapeutPatientService.getPatientenFuer(therapeut.getUuid());
        assert (patientenFuer).size() == 1;
        Optional<UserEntity> patient = userService.getByUsername(username);
        assert (patient).isPresent();
    }

    @Test
    public void addPatient_soll_Passwort_gehashed_speichern()
    {
        persistEmailParam();
        UserEntity therapeut = getStandardUserEntityBuilder().withTherapeut(true).build();
        userService.register(therapeut);

        String token = generateTokenAndValidate(therapeut);

        String passwort = "passwort";
        String username = "username";
        userController.addPatient("localhost", token, "vorname", "nachname", "email@test.de", username, 10);

        Optional<UserEntity> patient = userService.getByUsername(username);
        assert (patient.isPresent());
    }

    @Test
    public void showPatienten_soll_eigene_Patienten_anzeigen()
    {
        UserEntity therapeut = getStandardUserEntityBuilder().withTherapeut(true).build();
        UserEntity patient = getStandardUserEntityBuilder().withTherapeut(false).build();
        userService.register(therapeut);
        userService.register(patient);

        String token = generateTokenAndValidate(therapeut);

        therapeutPatientService.add(
                new TherapeutPatientEntity.BUILDER()
                        .withPatient(patient.getUuid())
                        .withTherapeut(therapeut.getUuid())
                        .build()
        );

        String response = userController.showPatienten(token);
        assert (response).contains(patient.getUsername());
        assert (!response.contains(patient.getPasswort()));
    }

    @Test
    public void resetPasswort()
    {
        persistEmailParam();
        String email = "test@test.de";
        String altesPasswort = "123";
        UserEntity patient = getStandardUserEntityBuilder()
                .withTherapeut(false)
                .withEmail(email)
                .withPasswort(hash(altesPasswort))
                .build();
        userService.register(patient);

        String response = userController.resetPassword("localhost", email);

        assert(response.contains("success"));
        Optional<UserEntity> byEmail = userService.getByEmail(email);
        assert(byEmail.isPresent());
        assert(!byEmail.get().getPasswort().equals(hash(altesPasswort)));
    }

    private String generateTokenAndValidate(UserEntity therapeut)
    {
        String token = generateToken(therapeut.getUuid());
        tokenService.addToken(token);
        return token;
    }

    private String getTokenFromResponse(String response)
    {
        return Arrays.stream(response.split("\""))
                .filter(s -> s.length() > 20)
                .findFirst()
                .orElse("");
    }

    private void persistEmailParam()
    {
        List<ParameterEntity> parameterEntities = EmailParameterTestDatenErzeuger.generateMailParameter();
        parameterEntities.forEach(p -> parameterService.persist(p));
    }

}