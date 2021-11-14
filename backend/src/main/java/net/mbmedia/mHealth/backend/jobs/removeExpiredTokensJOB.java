package net.mbmedia.mHealth.backend.jobs;

import net.mbmedia.mHealth.backend.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class removeExpiredTokensJOB
{
    private static final Logger log = LoggerFactory.getLogger(removeExpiredTokensJOB.class);

    @Scheduled(cron = "0 0 4 * * *")
    public void run() {
        TokenService tokenService = new TokenService();
        tokenService.removeExpired();
    }
}
