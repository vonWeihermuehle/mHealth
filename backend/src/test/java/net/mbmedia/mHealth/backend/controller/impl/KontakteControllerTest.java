package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.controller.IKontakteController;
import net.mbmedia.mHealth.backend.kontakt.IKontakteService;
import net.mbmedia.mHealth.backend.kontakt.impl.KontaktEntity;
import net.mbmedia.mHealth.backend.token.TokenService;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.kontakt.impl.KontakteEntityTestDatenErzeuger.getStandardKontakt;
import static net.mbmedia.mHealth.backend.kontakt.impl.KontakteEntityTestDatenErzeuger.getStandardKontakteBuilder;
import static net.mbmedia.mHealth.backend.token.TokenGenerator.generateToken;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntityBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KontakteControllerTest
{
    public static final UserEntity THERAPEUT = getStandardUserEntityBuilder().withTherapeut(true).build();
    public static final UserEntity PATIENT = getStandardUserEntityBuilder().withTherapeut(false).build();


    @Autowired
    private IUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IKontakteService kontakteService;

    @Autowired
    private IKontakteController kontakteController;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(userService, tokenService, kontakteService);
        registerUser(THERAPEUT, PATIENT);
    }

    @Test
    public void addKontakt()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);

        String response = kontakteController.add(token, "speziellerName", "art", "email", "0123", "123");

        assert(response).contains("success");
        List<KontaktEntity> all = kontakteService.getAll();
        assert(all).size() == 1;
    }

    @Test
    public void delKontakt()
    {
        KontaktEntity kontakt = getStandardKontakt();
        Optional<Long> id = kontakteService.addKontakt(kontakt);
        String token = generateAndAddTokenFuer(THERAPEUT);

        String response = kontakteController.remove(token, id.get());

        assert(response).contains("success");
        assert(kontakteService.getAll()).size() == 0;
    }

    @Test
    public void getAllFuer()
    {
        KontaktEntity kontakt = getStandardKontakt();
        KontaktEntity kontakt2 = getStandardKontakt();
        persistiereKontakte(kontakt, kontakt2);
        String token = generateAndAddTokenFuer(THERAPEUT);

        String response = kontakteController.getAllFuer(token, kontakt.getUserID());
        assert(response).contains(kontakt.getUserID());
        assert(!response.contains(kontakt2.getUserID()));
    }

    @Test
    public void getOwn()
    {
        String speziellerName = "speziellerName";
        KontaktEntity eigenerKontakt = getStandardKontakteBuilder().withName(speziellerName).withUserID(PATIENT.getUuid()).build();
        KontaktEntity fremderKontakt = getStandardKontakt();
        String token = generateAndAddTokenFuer(PATIENT);
        persistiereKontakte(eigenerKontakt, fremderKontakt);

        String response = kontakteController.getOwn(token);
        assert(response).contains(speziellerName);
        assert(!response.contains(fremderKontakt.getUserID()));
    }

    private void persistiereKontakte(KontaktEntity... kontakte)
    {
        Arrays.stream(kontakte).forEach(k -> kontakteService.addKontakt(k));
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