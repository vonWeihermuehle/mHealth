package net.mbmedia.mHealth.backend.fragebogen;

import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenEntity;
import net.mbmedia.mHealth.backend.util.ValueProvider;

import static net.mbmedia.mHealth.backend.util.UUIDHelper.generateUUID;
import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

public class FragebogenEntityTestdatenErzeuger
{
    private static final ValueProvider zufall = mitZufallswerten();

    public static FragebogenEntity createStandardFragebogenEntity()
    {
        return getStandardFragebogenEntityBuilder().build();
    }

    public static FragebogenEntity.BUILDER getStandardFragebogenEntityBuilder()
    {
        return new FragebogenEntity.BUILDER()
                .withAuthor(generateUUID())
                .withBeschreibung(zufall.loremIpsum(30))
                .withTitel("Fragebogen " + zufall.romanNumber())
                .withJson(getStandartFragebogenJson());
    }

    public static String getStandartFragebogenJson()
    {
        return "[{\"items\":[{\"name\":\"normaleTextfrageAbschnitt1\",\"label\":\"Normale Textfrage (Abschnitt 1)\",\"type\":\"string\",\"justAdded\":false,\"fieldValidations\":{\"rules\":[]},\"errors\":[]},{\"name\":\"einfachesRatingAbschnitt1\",\"label\":\"Einfaches Rating (Abschnitt 1)\",\"type\":\"numericRating\",\"justAdded\":false,\"fieldValidations\":{\"rules\":[]},\"errors\":[]},{\"name\":\"multipleChoiceAbschnitt1\",\"label\":\"Multiple Choice (Abschnitt 1)\",\"type\":\"radio\",\"justAdded\":false,\"items\":[{\"label\":\"Option A\",\"optionValue\":\"Option A\"},{\"label\":\"Option B\",\"optionValue\":\"Option B\"}],\"fieldValidations\":{\"rules\":[]},\"value\":\"\",\"errors\":[]}]},{\"items\":[{\"name\":\"einfacheTextFrageAbschnitt2\",\"label\":\"Einfache TextFrage (Abschnitt 2)\",\"type\":\"string\",\"justAdded\":false,\"fieldValidations\":{\"rules\":[]},\"errors\":[]},{\"name\":\"ratingAbschnitt2\",\"label\":\"Rating  (Abschnitt 2)\",\"type\":\"numericRating\",\"justAdded\":false,\"fieldValidations\":{\"rules\":[]},\"errors\":[]},{\"name\":\"multipleChoiceAbschnitt2\",\"label\":\"Multiple Choice  (Abschnitt 2)\",\"type\":\"radio\",\"justAdded\":false,\"items\":[{\"label\":\"Option A\",\"optionValue\":\"Option A\"},{\"label\":\"Option B\",\"optionValue\":\"Option B\"},{\"label\":\"Option C\",\"optionValue\":\"Option C\"}],\"fieldValidations\":{\"rules\":[]},\"value\":\"\",\"errors\":[]}]}]";
    }
}
