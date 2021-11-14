package net.mbmedia.mHealth.backend.mail;

public enum Placeholder
{
    GREETING("${NAME}"),
    USERNAME("${USERNAME}"),
    PASSWORD("${PASSWORD}"),
    LINK("${LINK}"),
    PATIENTNAME("${PATIENTNAME}"),
    SCHWELLWERT("${SCHWELLWERT}"),
    WERT("${WERT}");

    private final String text;

    Placeholder(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }
}