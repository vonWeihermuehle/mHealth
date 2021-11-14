package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.controller.IOrteController;
import net.mbmedia.mHealth.backend.ort.IOrteService;
import net.mbmedia.mHealth.backend.ort.impl.OrtEntity;
import net.mbmedia.mHealth.backend.token.TokenService;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import net.mbmedia.mHealth.backend.util.ValueProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.ort.impl.OrtEntityTestDatenErzeuger.standardOrtBuilder;
import static net.mbmedia.mHealth.backend.ort.impl.OrtEntityTestDatenErzeuger.standartOrt;
import static net.mbmedia.mHealth.backend.token.TokenGenerator.generateToken;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntityBuilder;
import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrteControllerTest
{
    public static final UserEntity THERAPEUT = getStandardUserEntityBuilder().withTherapeut(true).build();
    public static final UserEntity PATIENT = getStandardUserEntityBuilder().withTherapeut(false).build();
    private static final ValueProvider zufall = mitZufallswerten();

    @Autowired
    private IUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IOrteService orteService;

    @Autowired
    private IOrteController orteController;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(userService, tokenService, orteService);
        registerUser(THERAPEUT, PATIENT);
    }

    @Test
    public void addOrt()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        String response = orteController.addOrt(token, zufall.randomString(10), zufall.randomString(10), PATIENT.getUuid(), "123", "456");

        assert (response.contains("success"));
        assert (!orteService.getAll().isEmpty());
    }

    @Test
    public void delOrt()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        OrtEntity ort = standartOrt();
        Optional<Long> ortId = orteService.add(ort);
        assert (ortId.isPresent());
        String response = orteController.delOrt(token, ortId.get());

        assert (response.contains("success"));
        assert (orteService.getAll().isEmpty());
    }

    @Test
    public void getFuer()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        OrtEntity ort = standardOrtBuilder().withPatient(PATIENT.getUuid()).build();
        OrtEntity ortIrrelevant = standartOrt();
        persistiere(ort, ortIrrelevant);

        String response = orteController.getOrteFuerPatient(token, PATIENT.getUuid());
        assert (response.contains(ort.getBeschreibung()));
        assert (!response.contains(ortIrrelevant.getBeschreibung()));
    }

    @Test
    public void update()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        OrtEntity ort = standardOrtBuilder().withPatient(PATIENT.getUuid()).build();
        Optional<Long> ortId = orteService.add(ort);

        String besondere_beschreibung = "besondere Beschreibung";
        String response = orteController.updateOrt(token, ortId.get(), "", besondere_beschreibung, "", "");

        assert (orteService.getAll().get(0).getBeschreibung().equals(besondere_beschreibung));
    }

    private void registerUser(UserEntity... users)
    {
        Arrays.stream(users).forEach(u -> userService.register(u));
    }

    private void persistiere(OrtEntity... entities)
    {
        Arrays.stream(entities).forEach(o -> orteService.add(o));
    }

    private String generateAndAddTokenFuer(UserEntity therapeut)
    {
        String token = generateToken(therapeut.getUuid());
        tokenService.addToken(token);
        return token;
    }

}