package net.mbmedia.mHealth.backend.fragebogen;

import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenZuweisungEntity;

import java.time.LocalDate;

import static net.mbmedia.mHealth.backend.fragebogen.FragebogenEntityTestdatenErzeuger.createStandardFragebogenEntity;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntity;

public class FragebogenZuweisungEntityTestdatenErzeuger
{

    public static FragebogenZuweisungEntity createStandardFragebogenZuweisung()
    {
        return getStandardFragebogenZuweisungBuilder().build();
    }

    public static FragebogenZuweisungEntity.BUILDER getStandardFragebogenZuweisungBuilder()
    {
        return new FragebogenZuweisungEntity.BUILDER()
                .withCron("")
                .withFragebogen(createStandardFragebogenEntity())
                .withEmpfaenger(getStandardUserEntity())
                .withErstellt(LocalDate.now());
    }
}