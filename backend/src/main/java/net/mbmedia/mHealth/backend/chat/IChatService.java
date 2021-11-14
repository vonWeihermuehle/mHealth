package net.mbmedia.mHealth.backend.chat;

import net.mbmedia.mHealth.backend.chat.impl.TO.ChatTO;
import net.mbmedia.mHealth.backend.chat.impl.MessageEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IChatService
{
    Optional<Long> add(MessageEntity message);

    List<MessageEntity> getMessagesFrom(String authorID, String empfaengerID);

    List<ChatTO> getPartner(String userID);

    void markAsRead(String empfaengerId, String authorID);

    Optional<Long> checkForNewMessages(String empfaengerID, LocalDateTime date);

    void deleteAllMessagesFrom(String uuid);
}
