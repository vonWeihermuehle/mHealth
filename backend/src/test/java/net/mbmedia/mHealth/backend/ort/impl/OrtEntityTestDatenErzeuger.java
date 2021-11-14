package net.mbmedia.mHealth.backend.ort.impl;

import net.mbmedia.mHealth.backend.util.ValueProvider;

import static net.mbmedia.mHealth.backend.util.UUIDHelper.generateUUID;
import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

public class OrtEntityTestDatenErzeuger
{
    private static ValueProvider zufall = mitZufallswerten();

    public static OrtEntity standartOrt()
    {
        return standardOrtBuilder().build();
    }

    public static OrtEntity.BUILDER standardOrtBuilder()
    {
        return new OrtEntity.BUILDER()
                .withTitel(zufall.ort())
                .withBeschreibung(zufall.loremIpsum(35))
                .withLat("1.234")
                .withLng("1.234")
                .withPatient(generateUUID());
    }

}