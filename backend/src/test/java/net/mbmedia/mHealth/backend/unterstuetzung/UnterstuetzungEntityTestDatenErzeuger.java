package net.mbmedia.mHealth.backend.unterstuetzung;

import net.mbmedia.mHealth.backend.unterstuetzung.impl.UnterstuetzungEntity;
import net.mbmedia.mHealth.backend.util.ValueProvider;

import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

public class UnterstuetzungEntityTestDatenErzeuger
{
    private static ValueProvider zufall = mitZufallswerten();
    public static UnterstuetzungEntity standardUnterstuetzung()
    {
        return standardUnterstuetzungBuilder().build();
    }

    public static UnterstuetzungEntity.BUILDER standardUnterstuetzungBuilder()
    {
        return new UnterstuetzungEntity.BUILDER()
                .withAuthor("")
                .withEmpfaenger("")
                .withText(standardUebung())
                .withTitel("Übung " + zufall.romanNumber());
    }

    public static String standardUebung()
    {
        return "<p><em>Setze dich aufrecht hin. Atme nun so richtig tief ein. <br></em></p><p><em><br></em></p>&#10;<p><em>Dein Bauch w&#246;lbt sich bei der Einatmung nach au&#223;en. </em><em>Wenn du m&#246;chtest, leg dabei eine Hand auf deinen Bauchnabel, dann kannst du besser merken, ob du es richtig machst. </em><em>Z&#228;hle beim Einatmen bis 4. <br></em></p><p><em><br></em></p>&#10;<p><em>Atme nun auch 4 Sekunden wieder aus. Deine Bauchdecke geht dabei nach innen, Richtung Wirbels&#228;ule. </em></p>&#10;<p><em>Wiederhole diese Atmung mindestens 5 Mal und bleibe m&#246;glichst mit deiner Konzentration beim Atmen und beim Z&#228;hlen.&#160;</em></p>&#10;<p><em>Wenn du m&#246;chtest, kannst du versuchen, deine Ausatmung immer &#10;l&#228;nger werden zu lassen. Dann geht es so: 4 ein &#8211; 4 aus, 4 ein &#8211; 5 aus, 4&#10; ein &#8211; 6 aus &#8230; </em><em>Bis 8 kannst du das machen. <br></em></p><p><em><br></em></p>&#10;<p><em>Sp&#252;re nach, wie sich dein K&#246;rper nun anf&#252;hlt. <br></em></p><p><em>F&#252;hlst du dich vitaler, frischer, wacher oder entspannter?</em></p>";
    }
}
