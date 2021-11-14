package net.mbmedia.mHealth.backend.jobs;

import jdk.nashorn.internal.ir.annotations.Ignore;
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
    @Ignore
    public void runZuweisungenJob()
    {
        cronZuweisungenJOB.run();
    }

    @Test
    @Ignore
    public void runTokenJob()
    {
        removeExpiredTokensJOB.run();
    }




}
