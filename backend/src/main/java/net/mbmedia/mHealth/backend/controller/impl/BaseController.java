package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.token.TokenGenerator;
import net.mbmedia.mHealth.backend.token.TokenService;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import net.mbmedia.mHealth.backend.user.impl.UserJPA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

import static java.util.Optional.empty;
import static net.mbmedia.mHealth.backend.util.FailureAnswer.SOME;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.failureAnswer;


public class BaseController {
    protected final IUserService userService = new UserJPA();
    protected final TokenService tokenService = new TokenService();

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    protected boolean isTokenValid(String token) {
        return !tokenService.isTokenExpired(token);
    }

    protected boolean isTechUser(String token) {
        Optional<String> uuid = getUserIDFromToken(token);
        if (!uuid.isPresent()) {
            return false;
        }

        Optional<UserEntity> User = userService.getById(uuid.get());
        return User.isPresent() && User.get().isTechUser();
    }

    protected boolean isTherapeut(Optional<String> userID) {
        if (!userID.isPresent()) {
            return false;
        }

        Optional<UserEntity> User = userService.getById(userID.get());
        return User.isPresent() && User.get().isTherapeut();
    }

    protected boolean isPatient(Optional<String> userID) {
        if (!userID.isPresent()) {
            return false;
        }

        Optional<UserEntity> User = userService.getById(userID.get());
        return User.isPresent() && !User.get().isTherapeut();
    }

    protected Optional<String> getUserIDFromToken(String token) {
        try {
            return Optional.of(TokenGenerator.decodeJWT(token).getSubject());
        } catch (Exception e) {
            log.error("Token nicht decodable: " + token);
            return empty();
        }
    }

    @ExceptionHandler({RuntimeException.class})
    public String handleException() {
        return failureAnswer(SOME);
    }
}
