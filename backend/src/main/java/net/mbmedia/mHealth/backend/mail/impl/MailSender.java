package net.mbmedia.mHealth.backend.mail.impl;

import net.mbmedia.mHealth.backend.mail.IMailService;
import net.mbmedia.mHealth.backend.mail.MailTextMapper;
import net.mbmedia.mHealth.backend.param.IParameterService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Optional;
import java.util.Properties;

import static net.mbmedia.mHealth.backend.mail.MailParameter.*;

@Service
public class MailSender implements IMailService
{
    private static final Logger log = LoggerFactory.getLogger(MailSender.class);
    public static final String NO_REPLAY_MAIL = "noReply@mHealth.de";

    @Autowired
    private IParameterService parameterService;

    private Optional<String> hostname;
    private Optional<Integer> port;
    private Optional<Boolean> auth;
    private Optional<String> username;
    private Optional<String> password;
    private Optional<Boolean> startTLS;

    @Override
    public void sendRegisterConfirmation(String host, UserEntity to, String passwort)
    {
        String message = new MailTextMapper()
                .withTemplate(parameterService.getStringParam(REGISTRATION_LOGIN_DATA.getSchluessel()).get())
                .withUsername(to.getUsername())
                .withPasswort(passwort)
                .withLink("http://" + host + "/login")
                .withName(to.getFullName())
                .build();
        sendSimpleMail(NO_REPLAY_MAIL, to.getEmail(), message, "Ihr Benutzerkonto bei mHealth");
    }

    @Override
    public void sendNewPasswort(String host, UserEntity to, String passwort)
    {
        String message = new MailTextMapper()
                .withTemplate(parameterService.getStringParam(RESET_PASSWORT.getSchluessel()).get())
                .withPasswort(passwort)
                .withLink("http://" + host + "/login")
                .withName(to.getFullName())
                .build();
        sendSimpleMail(NO_REPLAY_MAIL, to.getEmail(), message, "Passwort bei mHealth");
    }

    @Override
    public void sendSchwellWertUeberschritten(UserEntity patient, UserEntity therapeut, int wert)
    {
        String message = new MailTextMapper()
                .withTemplate(parameterService.getStringParam(THRESHOLD.getSchluessel()).get())
                .withName(therapeut.getFullName())
                .withUsername(patient.getUsername())
                .withSchwellwert(patient.getSchwellwert())
                .withWert(wert)
                .build();
        sendSimpleMail(NO_REPLAY_MAIL, therapeut.getEmail(), message, "Schwellwert Überschritten");
    }

    private void sendSimpleMail(String from, String to, String msg, String subject)
    {
        if (!init())
        {
            log.error("Mail cant be initialized");
            return;
        }

        Properties props = getProperties();

        Session session = Session.getDefaultInstance(props, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username.get(), password.get());
            }

        });
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            BodyPart htmlBodyPart = new MimeBodyPart();
            htmlBodyPart.setContent(msg, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlBodyPart);

            message.setContent(multipart);

            final Message final_message = message;

            log.info("Email created");

            Runnable emailworker = () ->
            {
                try
                {
                    Transport.send(final_message);
                    log.info("Email sent");
                } catch (MessagingException e)
                {
                    e.printStackTrace();
                }
            };
            emailworker.run();

        } catch (MessagingException e)
        {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


    private boolean init()
    {
        //IParameterService parameterService = new ParamterJPA();

        try
        {
            this.hostname = parameterService.getStringParam(HOST.getSchluessel());
            this.port = parameterService.getIntParam(Port.getSchluessel());

            this.auth = parameterService.getBoolParam(AUTH.getSchluessel());

            if (this.auth.get())
            {
                this.username = parameterService.getStringParam(Username.getSchluessel());
                this.password = parameterService.getStringParam(PW.getSchluessel());
                this.startTLS = parameterService.getBoolParam(StartTLS.getSchluessel());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Properties getProperties()
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", hostname.get());
        props.put("mail.smtp.port", port.get());
        if (auth.get())
        {
            props.put("mail.smtp.auth", true);
            props.put("mail.smtp.starttls.enable", startTLS.get());
        } else
        {
            props.put("mail.smtp.auth", false);
        }

        return props;
    }
}
