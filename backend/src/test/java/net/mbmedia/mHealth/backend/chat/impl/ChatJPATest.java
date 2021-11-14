package net.mbmedia.mHealth.backend.chat.impl;

import net.mbmedia.mHealth.backend.chat.IChatService;
import net.mbmedia.mHealth.backend.chat.impl.TO.ChatTO;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import net.mbmedia.mHealth.backend.util.UUIDHelper;
import net.mbmedia.mHealth.backend.util.ValueProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.chat.impl.MessageEntityTestdatenErzeuger.getStandardMessageBuilder;
import static net.mbmedia.mHealth.backend.chat.impl.MessageEntityTestdatenErzeuger.standardMessage;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntity;
import static net.mbmedia.mHealth.backend.util.ValueProvider.mitZufallswerten;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatJPATest
{

    @Autowired
    private IChatService chatService;

    @Autowired
    private IUserService userService;

    private ChatJPA chatJPA = new ChatJPA();

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(chatService, userService);
    }

    @Test
    public void add()
    {
        Optional<Long> id = chatService.add(standardMessage());

        assert (id.isPresent());
        assert (!chatJPA.getAll().isEmpty());
    }

    @Test
    public void add_soll_mit_Uhrzeit_speichern()
    {
        Optional<Long> id = chatService.add(standardMessage());
        assert (id.isPresent());

        Optional<MessageEntity> persitierteNachricht = chatJPA.getAll().stream().filter(c -> c.getId().equals(id.get())).findFirst();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String format = formatter.format(persitierteNachricht.get().getErstellt());
        assert(!format.equals("00:00"));
    }

    @Test
    public void getFrom()
    {
        String eigeneID = UUIDHelper.generateUUID();
        MessageEntity relevant = getStandardMessageBuilder()
                .withEmpfaengerID(eigeneID)
                .build();
        MessageEntity relevant2 = getStandardMessageBuilder()
                .withAuthorID(relevant.getAuthorID())
                .withEmpfaengerID(eigeneID)
                .build();
        MessageEntity irrelevant = standardMessage();
        persist(relevant, relevant2, irrelevant);

        List<MessageEntity> messagesFrom = chatService.getMessagesFrom(relevant.getAuthorID(), eigeneID);

        assert (messagesFrom.size()) == 2;
        messagesFrom.forEach(m ->
        {
            assert (m.getAuthorID().equals(relevant.getAuthorID()));
            assert (m.getEmpfaengerID().equals(eigeneID));
        });
    }

    @Test
    public void getFrom_liefert_max_die_letzten_100_Nachrichten()
    {
        ValueProvider zufall = mitZufallswerten();
        String eigeneID = UUIDHelper.generateUUID();
        String authorID = UUIDHelper.generateUUID();

        persistMindestens100(authorID, eigeneID, zufall);

        List<MessageEntity> messagesFrom = this.chatService.getMessagesFrom(authorID, eigeneID);
        assert (messagesFrom.size()) == 100;

    }

    @Test
    public void getPartner()
    {
        UserEntity partner = getStandardUserEntity();
        String eigeneID = UUIDHelper.generateUUID();
        MessageEntity relevant = getStandardMessageBuilder()
                .withEmpfaengerID(eigeneID)
                .withAuthorID(partner.getUuid())
                .build();
        persist(relevant);
        userService.register(partner);

        List<ChatTO> all = chatService.getPartner(eigeneID);
        assert(!all.isEmpty());
        assert(all.get(0).getUsername()).equals(partner.getUsername());
    }

    @Test
    public void deleteMessagesFrom()
    {
        MessageEntity relevant = getStandardMessageBuilder().build();
        persist(relevant);

        chatService.deleteAllMessagesFrom(relevant.getAuthorID());

        List<MessageEntity> all = chatJPA.getAll();
        assert(all.isEmpty());
    }

    private void persistMindestens100(String authorID, String empfaenger, ValueProvider zufall)
    {
        int count = zufall.intNumber(100, 200);
        MessageEntity.BUILDER builder = getStandardMessageBuilder()
                .withAuthorID(authorID)
                .withEmpfaengerID(empfaenger);
        for (int i = 0; i < count; i++)
        {
            MessageEntity build = builder.withNachricht(zufall.randomString(255)).build();
            this.chatService.add(build);
        }
    }

    private void persist(MessageEntity... entities)
    {
        for (MessageEntity entity : entities)
        {
            this.chatService.add(entity);
        }
    }

}