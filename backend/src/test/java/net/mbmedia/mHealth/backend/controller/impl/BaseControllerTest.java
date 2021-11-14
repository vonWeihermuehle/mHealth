package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.token.TokenService;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.token.TokenGenerator.generateToken;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntity;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntityBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseControllerTest
{

    @Autowired
    private IUserService userService;

    @Autowired
    private TokenService tokenService;

    private final BaseController controller = new BaseController();

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables( userService, tokenService);
    }

    @Test
    public void soll_UserID_erkennen()
    {
        UserEntity standardUserEntity = getStandardUserEntity();
        String token = generateToken(standardUserEntity.getUuid());
        userService.register(standardUserEntity);
        tokenService.addToken(token);

        String userIDFromToken = controller.getUserIDFromToken(token).get();
        assert (userIDFromToken).equals(standardUserEntity.getUuid());
    }

    @Test
    public void soll_Patient_erkennen()
    {
        UserEntity Patient = getStandardUserEntityBuilder()
                .withTherapeut(false)
                .build();
        String token = generateToken(Patient.getUuid());
        userService.register(Patient);
        tokenService.addToken(token);
        Optional<String> userID = controller.getUserIDFromToken(token);
        assert (controller.isPatient(userID));
    }

    @Test
    public void soll_Therapeut_erkennen()
    {
        UserEntity Therapeut = getStandardUserEntityBuilder()
                .withTherapeut(true)
                .build();
        String token = generateToken(Therapeut.getUuid());
        userService.register(Therapeut);
        tokenService.addToken(token);
        Optional<String> userIDFromToken = controller.getUserIDFromToken(token);
        assert (controller.isTherapeut(userIDFromToken));
    }

    @Test
    public void soll_ungueltige_oder_abgelaufene_Token_zurueckweisen()
    {
        UserEntity user1 = getStandardUserEntity();
        UserEntity user2 = getStandardUserEntity();
        String gueltig = generateToken(user1.getUuid());
        String ungueltig = generateToken(user2.getUuid());
        String sonstiges = "asd";

        tokenService.addToken(gueltig);

        assert controller.isTokenValid(gueltig);
        assert(!controller.isTokenValid(ungueltig));
        assert(!controller.isTokenValid(sonstiges));
    }

}