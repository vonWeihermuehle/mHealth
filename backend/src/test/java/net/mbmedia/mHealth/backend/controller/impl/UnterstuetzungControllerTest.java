package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.controller.IUnterstuetzungController;
import net.mbmedia.mHealth.backend.token.TokenService;
import net.mbmedia.mHealth.backend.unterstuetzung.IUnterstuetzungService;
import net.mbmedia.mHealth.backend.unterstuetzung.impl.UnterstuetzungEntity;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import net.mbmedia.mHealth.backend.util.FailureAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.token.TokenGenerator.generateToken;
import static net.mbmedia.mHealth.backend.unterstuetzung.UnterstuetzungEntityTestDatenErzeuger.standardUnterstuetzungBuilder;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntityBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnterstuetzungControllerTest
{

    public static final UserEntity THERAPEUT = getStandardUserEntityBuilder().withTherapeut(true).build();
    public static final UserEntity PATIENT = getStandardUserEntityBuilder().withTherapeut(false).build();

    @Autowired
    private IUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IUnterstuetzungService unterstuetzungService;

    @Autowired
    private IUnterstuetzungController unterstuetzungController;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(userService, tokenService, unterstuetzungService);
        registerUser(THERAPEUT, PATIENT);
    }

    @Test
    public void soll_Unterstuetzungen_hinzufuegen()
    {
        UnterstuetzungEntity unterstuetzung = standardUnterstuetzungBuilder()
                .withAuthor(THERAPEUT.getUuid())
                .withEmpfaenger(PATIENT.getUuid())
                .build();
        String token = generateAndAddTokenFuer(THERAPEUT);

        unterstuetzungController.addUebung(token, unterstuetzung.getTitel(), unterstuetzung.getText(), PATIENT.getUuid());

        assert (unterstuetzungService.getAll().size()) == 1;
    }

    @Test
    public void soll_Uebungen_Fuer_Patient_anzeigen()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);

        unterstuetzungController.addUebung(token, "titel", "text", PATIENT.getUuid());

        String uebungenFuerPatient = unterstuetzungController.getUebungenFuerPatient(token, PATIENT.getUuid());
        assert(uebungenFuerPatient).contains("titel");
    }

    @Test
    public void soll_eigene_Uebungen_anzeigen()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        String patientToken = generateAndAddTokenFuer(PATIENT);

        unterstuetzungController.addUebung(token, "titel", "text", PATIENT.getUuid());

        String uebungen = unterstuetzungController.getUebungen(patientToken);
        assert(uebungen).contains("titel");
    }

    @Test
    public void soll_einzelne_Uebung_loeschen()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        unterstuetzungController.addUebung(token, "titel", "text", PATIENT.getUuid());
        unterstuetzungController.addUebung(token, "anderer", "anderer2", PATIENT.getUuid());

        List<UnterstuetzungEntity> uebungen = unterstuetzungService.getAllFuer(PATIENT.getUuid());
        List<Long> UebungenIds = uebungen.stream().map(UnterstuetzungEntity::getId).collect(Collectors.toList());
        Optional<Long> IdZuLoeschen = UebungenIds.stream().findFirst();

        assert (UebungenIds).size() == 2;

        unterstuetzungController.delUebung(token, IdZuLoeschen.get());

        List<UnterstuetzungEntity> uebungenDanach = unterstuetzungService.getAllFuer(PATIENT.getUuid());
        assert (uebungenDanach).size() == 1;
        boolean empty = uebungenDanach.stream()
                .map(UnterstuetzungEntity::getId)
                .noneMatch(l -> Objects.equals(l, IdZuLoeschen.get()));
        assert (empty);
    }

    @Test
    public void darf_Uebung_nur_loeschen_wenn_selbst_erfasst()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        String response = unterstuetzungController.delUebung(token, 7000L);
        assert(response).contains(FailureAnswer.NO_PERMISSION.getMessage());
    }

    private String generateAndAddTokenFuer(UserEntity therapeut)
    {
        String token = generateToken(therapeut.getUuid());
        tokenService.addToken(token);
        return token;
    }

    private void registerUser(UserEntity... users)
    {
        Arrays.stream(users).forEach(u -> userService.register(u));
    }

}