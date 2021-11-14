package net.mbmedia.mHealth.backend.mail.impl;

import jdk.nashorn.internal.ir.annotations.Ignore;
import net.mbmedia.mHealth.backend.mail.IMailService;
import net.mbmedia.mHealth.backend.param.IParameterService;
import net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.mail.impl.EmailParameterTestDatenErzeuger.generateMailParameter;

@Ignore
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MailSenderTest
{

    @Autowired
    private IParameterService parameterService;

    @Autowired
    private IMailService mailService;

    @BeforeEach
    public void before()
    {
        truncateAllTables(parameterService);
        generateMailParameter().forEach(p -> parameterService.persist(p));
    }

    @Test
    public void sendRegisterMail()
    {
        UserEntity user = UserEntityTestDatenErzeuger.getStandardUserEntity();
        mailService.sendRegisterConfirmation("localhost", user, "asdf");
    }

}