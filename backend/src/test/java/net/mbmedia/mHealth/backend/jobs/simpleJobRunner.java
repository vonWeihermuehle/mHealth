package net.mbmedia.mHealth.backend.jobs;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class simpleJobRunner
{
    @Autowired
    private setCronZuweisungenJOB cronZuweisungenJOB;

    @Autowired
    private removeExpiredTokensJOB removeExpiredTokensJOB;

    @Test
    @Disabled
    public void runZuweisungenJob()
    {
        cronZuweisungenJOB.run();
    }

    @Test
    @Disabled
    public void runTokenJob()
    {
        removeExpiredTokensJOB.run();
    }




}
