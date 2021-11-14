package net.mbmedia.mHealth.backend.mail;


public enum MailParameter
{

    HOST("mail.host"),
    Port("mail.port"),
    AUTH("mail.auth"),
    Username("mail.username"),
    PW("mail.password"),
    StartTLS("mail.starttls"),

    REGISTRATION_LOGIN_DATA("mail.register.login"),
    RESET_PASSWORT("mail.reset.pw"),
    THRESHOLD("mail.schwellwert");

    private final String schluessel;

    MailParameter(String s)
    {
        this.schluessel = s;
    }

    public String getSchluessel()
    {
        return this.schluessel;
    }

}
