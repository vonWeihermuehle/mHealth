package net.mbmedia.mHealth.backend.chat.impl;


import net.mbmedia.mHealth.backend.util.UUIDHelper;
import net.mbmedia.mHealth.backend.util.ValueProvider;

import static java.time.LocalDateTime.now;
import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

public class MessageEntityTestdatenErzeuger
{
    private static final ValueProvider zufall = mitZufallswerten();

    public static MessageEntity standardMessage()
    {
        return getStandardMessageBuilder().build();
    }

    public static MessageEntity.BUILDER getStandardMessageBuilder()
    {
        return new MessageEntity.BUILDER()
                .withAuthorID(UUIDHelper.generateUUID())
                .withEmpfaengerID(UUIDHelper.generateUUID())
                .withErstellt(now())
                .withNachricht(zufall.randomString(255))
                .withGelesen(false);
    }
}