package net.mbmedia.mHealth.backend.user;

import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import net.mbmedia.mHealth.backend.util.CryptoHelper;
import net.mbmedia.mHealth.backend.util.UUIDHelper;
import net.mbmedia.mHealth.backend.util.ValueProvider;

import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

public class UserEntityTestDatenErzeuger
{
    public static final String STANDARD_PASSWORT = "123";
    private static final ValueProvider zufall = mitZufallswerten();

    public static UserEntity getStandardUserEntity()
    {
        return getStandardUserEntityBuilder().build();
    }

    public static UserEntity getStandardPatient()
    {
        return getStandardUserEntityBuilder().build();
    }

    public static UserEntity getStandardTherapeut()
    {
        return getStandardUserEntityBuilder()
                .withTherapeut(true)
                .withSchwellwert(null)
                .build();
    }

    public static UserEntity.BUILDER getStandardUserEntityBuilder()
    {
        ValueProvider.Namen namen = zufall.namenkombiniert();

        String familienname = namen.getFamilienname();
        String vorname = namen.getVorname();
        return new UserEntity.BUILDER()
                .withUUID(UUIDHelper.generateUUID())
                .withEmail(zufall.emailAddress(familienname, vorname))
                .withNachname(familienname)
                .withVorname(vorname)
                .withPasswort(CryptoHelper.hash(STANDARD_PASSWORT))
                .withTherapeut(false)
                .withLat((double) zufall.intNumber(0, 100))
                .withLng((double) zufall.intNumber(0, 100))
                .withUsername(namen.getUsername())
                .withTechUser(false)
                .withSchwellwert(zufall.intNumber());
    }
}
