package net.mbmedia.mHealth.backend.kontakt.impl;

public enum KontaktArt
{
    BEZUGSPERSON("Bezugsperson"),
    BERATUNGSSTELLE("Beratungsstelle");


    String value;
    KontaktArt(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }
}
