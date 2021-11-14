package net.mbmedia.mHealth.backend.mail;

import java.util.Arrays;

import static net.mbmedia.mHealth.backend.mail.Placeholder.*;

public class MailTextMapper
{
    private String message;

    public MailTextMapper withTemplate(String template)
    {
        this.message = template;
        return this;
    }

    public MailTextMapper withName(String name)
    {
        this.message = this.message.replace(GREETING.getText(), name);
        return this;
    }

    public MailTextMapper withUsername(String username)
    {
        this.message = this.message.replace(USERNAME.getText(), username);
        return this;
    }

    public MailTextMapper withPasswort(String passwort)
    {
        this.message = this.message.replace(PASSWORD.getText(), passwort);
        return this;
    }

    public MailTextMapper withLink(String link)
    {
        this.message = this.message.replace(LINK.getText(), link);
        return this;
    }

    public MailTextMapper withPatientName(String username)
    {
        this.message = this.message.replace(PATIENTNAME.getText(), username);
        return this;
    }

    public MailTextMapper withWert(int wert)
    {
        this.message = this.message.replace(WERT.getText(), "" + wert);
        return this;
    }

    public MailTextMapper withSchwellwert(int schwellwert)
    {
        this.message = this.message.replace(SCHWELLWERT.getText(), "" + schwellwert);
        return this;
    }

    public String build()
    {
        replaceEmptyPlaceholder();
        return this.message;
    }

    private void replaceEmptyPlaceholder()
    {
        Arrays.stream(values()).forEach(p -> this.message = this.message.replace(p.getText(), ""));
    }
}
