package net.mbmedia.mHealth.backend.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.util.CryptoHelper.hash;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TokenServiceTest
{

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(tokenService);
    }

    @Test
    public void addToken_soll_Token_mit_aktuellem_Datum_hinzufuegen()
    {
        String token = "asdf";
        tokenService.addToken(token);

        Optional<TokenEntity> tokenByHash = tokenService.getTokenByHash(hash(token));
        assert (tokenByHash).isPresent();

        boolean tokenExpired = tokenService.isTokenExpired(token);
        assert(!tokenExpired);
    }

}