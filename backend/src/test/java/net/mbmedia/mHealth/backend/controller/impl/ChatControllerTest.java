package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.chat.impl.ChatJPA;
import net.mbmedia.mHealth.backend.chat.impl.MessageEntity;
import net.mbmedia.mHealth.backend.controller.IChatController;
import net.mbmedia.mHealth.backend.token.TokenService;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.now;
import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static net.mbmedia.mHealth.backend.chat.impl.MessageEntityTestdatenErzeuger.getStandardMessageBuilder;
import static net.mbmedia.mHealth.backend.token.TokenGenerator.generateToken;
import static net.mbmedia.mHealth.backend.user.UserEntityTestDatenErzeuger.getStandardUserEntityBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerTest
{

    public static final UserEntity THERAPEUT = getStandardUserEntityBuilder().withTherapeut(true).build();
    public static final UserEntity PATIENT = getStandardUserEntityBuilder().withTherapeut(false).build();

    @Autowired
    private IUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IChatController chatController;

    private ChatJPA chatJPA = new ChatJPA();


    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(userService, tokenService, chatJPA);
        registerUser(THERAPEUT, PATIENT);
    }

    @Test
    public void add()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        String nachricht = "Ein ganz besondere Nachricht";
        chatController.add(token, nachricht, THERAPEUT.getUuid());

        List<MessageEntity> all = chatJPA.getAll();
        assert (all.size()) == 1;
        MessageEntity entity = all.get(0);
        assert (entity.getNachricht()).equals(nachricht);
        assert (entity.getEmpfaengerID()).equals(THERAPEUT.getUuid());
    }

    @Test
    public void getAllFrom()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        String flagRichtig = "richtige Nachricht";
        String flagFalsch = "FLAG_FALSCHE_NACHRICHT";
        MessageEntity nachricht = getStandardMessageBuilder()
                .withEmpfaengerID(THERAPEUT.getUuid())
                .withAuthorID(PATIENT.getUuid())
                .withNachricht(flagRichtig)
                .build();
        MessageEntity irrelevant = getStandardMessageBuilder().withNachricht(flagFalsch).build();
        chatJPA.add(nachricht);
        chatJPA.add(irrelevant);

        String response = chatController.getMessagesFrom(token, PATIENT.getUuid());

        assert (response.contains(flagRichtig));
        assert (!response.contains(flagFalsch));
    }

    @Test
    public void checkForNewMessages()
    {
        String token = generateAndAddTokenFuer(THERAPEUT);
        MessageEntity ungelesen = getStandardMessageBuilder()
                .withEmpfaengerID(THERAPEUT.getUuid())
                .withGelesen(false)
                .build();
        MessageEntity gelesen = getStandardMessageBuilder()
                .withEmpfaengerID(THERAPEUT.getUuid())
                .withGelesen(true)
                .build();
        MessageEntity vorLetztemCheck = getStandardMessageBuilder()
                .withEmpfaengerID(THERAPEUT.getUuid())
                .withGelesen(false)
                .withErstellt(now().minusDays(10))
                .build();
        MessageEntity nachLetztemCheck = getStandardMessageBuilder()
                .withEmpfaengerID(THERAPEUT.getUuid())
                .withGelesen(false)
                .withErstellt(now().plusDays(1))
                .build();

        MessageEntity irrelevant = getStandardMessageBuilder()
                .withGelesen(false)
                .build();
        persist(ungelesen, gelesen, vorLetztemCheck, nachLetztemCheck, irrelevant);

        String s = chatController.checkForNewMessages(token, getDateNowAsString());

        assert(s != null);
        assert(s.contains("2"));
        assert(!s.contains("3") && !s.contains("4"));
    }

    private String getDateNowAsString()
    {
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDate.format(formatter);
    }

    private String generateAndAddTokenFuer(UserEntity therapeut)
    {
        String token = generateToken(therapeut.getUuid());
        tokenService.addToken(token);
        return token;
    }

    private void persist(MessageEntity... entities)
    {
        Arrays.stream(entities).forEach(e -> chatJPA.add(e));
    }


    private void registerUser(UserEntity... users)
    {
        Arrays.stream(users).forEach(u -> userService.register(u));
    }

}