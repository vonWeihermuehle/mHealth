package net.mbmedia.mHealth.backend.fragebogen;

import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenAbgeschlossenEntity;
import net.mbmedia.mHealth.backend.util.ValueProvider;

import static java.time.LocalDate.now;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntity;
import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

public class FragebogenAbgeschlossenEntityTestdatenErzeuger
{
    private static ValueProvider zufall = mitZufallswerten();

    public static FragebogenAbgeschlossenEntity createStandardFragebogenZuweisung()
    {
        return getStandardFragebogenAbgeschlossenBuilder().build();
    }

    public static FragebogenAbgeschlossenEntity.BUILDER getStandardFragebogenAbgeschlossenBuilder()
    {
        return new FragebogenAbgeschlossenEntity.BUILDER()
                .withPatient(getStandardUserEntity())
                .withTitel(zufall.randomString(10))
                .withBeschreibung(zufall.randomString(30))
                .withErgebnis(getStandardErgebnis())
                .withWert(zufall.intNumber(0, 100))
                .withErstellt(now());
    }

    public static String getStandardErgebnis()
    {
        return "{\"normaleTextfrageAbschnitt1\":\"Antwort in Abschnitt 1\",\"einfachesRatingAbschnitt1\":2,\"multipleChoiceAbschnitt1\":\"Option A\",\"einfacheTextFrageAbschnitt2\":\"Antwort in Abschnitt 2\",\"ratingAbschnitt2\":2,\"multipleChoiceAbschnitt2\":\"Option A\"}";
    }
}
