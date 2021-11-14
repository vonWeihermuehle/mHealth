package net.mbmedia.mHealth.backend.jobs;

import net.mbmedia.mHealth.backend.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class removeExpiredTokensTest
{
    @Autowired
    private removeExpiredTokensJOB removeExpiredTokensJOB;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(tokenService);
    }

    @Test
    public void run()
    {
        removeExpiredTokensJOB.run();
    }
}