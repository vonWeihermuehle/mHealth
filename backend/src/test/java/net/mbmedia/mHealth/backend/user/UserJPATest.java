package net.mbmedia.mHealth.backend.user;

import net.mbmedia.mHealth.backend.token.TokenService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntity;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntityBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserJPATest
{
    @Autowired
    private IUserService userService;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void beforeEach(){
        truncateAllTables( userService, tokenService);
    }

    @Test
    public void register_und_login_sollen_funktionieren(){
        UserEntity userEntity = getStandardUserEntity();
        userService.register(userEntity);

        List<UserEntity> entities = userService.login(userEntity.getUsername(), userEntity.getPasswort());
        assert(entities).size() == 1;
        assert(entities.get(0)).getUuid().equals(userEntity.getUuid());
    }

    @Test
    public void setLastCoordinates()
    {
        UserEntity user = getStandardUserEntity();
        userService.register(user);
        double lat = 1.0;
        double lng = -2.0;

        userService.setLastCoordinates(user.getUuid(), lat, lng);

        Optional<UserEntity> byId = userService.getById(user.getUuid());
        assert(byId.get().getLat().equals(lat));
        assert byId.get().getLng().equals(lng);
    }

    @Test
    public void setSchwellwert()
    {
        UserEntity user = getStandardUserEntityBuilder()
                .withSchwellwert(0)
                .build();
        userService.register(user);

        int schwellwert = 100;
        userService.setSchwellwert(user.getUuid(), schwellwert);

        Optional<UserEntity> byId = userService.getById(user.getUuid());
        assert(byId.get().getSchwellwert()) == schwellwert;
    }

    @Test
    public void deleteUserById()
    {
        UserEntity user = getStandardUserEntity();
        userService.register(user);

        userService.deleteUserById(user.getUuid());

        Optional<UserEntity> byId = userService.getById(user.getUuid());
        assert(!byId.isPresent());
    }

    @Test
    public void isUsernameOrEmailUnused()
    {
        UserEntity user = getStandardUserEntity();
        userService.register(user);

        boolean sollteFalseSein = userService.isEmailAndUsernameUnused(user.getUsername(), "");
        boolean sollteFalseSein2 = userService.isEmailAndUsernameUnused("", user.getEmail());
        boolean sollteTrue = userService.isEmailAndUsernameUnused("was_anderes", "andere_emial");

        assert(!sollteFalseSein);
        assert(!sollteFalseSein2);
        assert(sollteTrue);
    }
}