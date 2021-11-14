package net.mbmedia.mHealth.backend.kontakt.impl;

import net.mbmedia.mHealth.backend.util.UUIDHelper;
import net.mbmedia.mHealth.backend.util.ValueProvider;

import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

public class KontakteEntityTestDatenErzeuger
{
    public static final ValueProvider zufall = mitZufallswerten();

    public static KontaktEntity getStandardKontakt()
    {
        return getStandardKontakteBuilder()
                .build();
    }

    public static KontaktEntity.BUILDER getStandardKontakteBuilder()
    {
        String familienname = zufall.familienname();
        String vorname = zufall.vorname();
        return new KontaktEntity.BUILDER()
                .withArt(getZufaelligeKontaktArt())
                .withEmail(zufall.emailAddress(familienname, vorname))
                .withName(familienname + " " + vorname)
                .withUserID(UUIDHelper.generateUUID())
                .withPhone(zufall.telefonnummer());
    }

    public static String getZufaelligeKontaktArt()
    {
        return zufall.getRandomEnum(KontaktArt.class).getValue();
    }
}
