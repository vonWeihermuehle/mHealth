package net.mbmedia.mHealth.backend.controller.impl;

import net.mbmedia.mHealth.backend.chat.IChatService;
import net.mbmedia.mHealth.backend.chat.impl.MessageEntity;
import net.mbmedia.mHealth.backend.chat.impl.TO.ChatTO;
import net.mbmedia.mHealth.backend.controller.IChatController;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static net.mbmedia.mHealth.backend.util.FailureAnswer.SOME;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.failureAnswer;
import static net.mbmedia.mHealth.backend.util.RejectUtils.rejectIf;
import static net.mbmedia.mHealth.backend.util.ResponseHelper.*;

@RestController
@RequestMapping(path = "/api/chat")
public class ChatController extends BaseController implements IChatController
{

    @Autowired
    private IChatService chatService;

    @PostMapping("/add")
    @Override
    public String add(String token, String nachricht, String empfaengerID)
    {
        Optional<String> userID = getUserIDFromToken(token);
        Optional<UserEntity> empfaengerIDOptional = userService.getById(empfaengerID);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !empfaengerIDOptional.isPresent());

        MessageEntity message = new MessageEntity.BUILDER()
                .withNachricht(nachricht)
                .withAuthorID(userID.get())
                .withEmpfaengerID(empfaengerID)
                .withErstellt(now())
                .build();

        Optional<Long> id = chatService.add(message);
        if (id.isPresent())
        {
            return simpleSuccessAnswer();
        }

        return failureAnswer(SOME);
    }

    @PostMapping("/get")
    @Override
    public String getMessagesFrom(String token, String authorID)
    {
        Optional<String> userID = getUserIDFromToken(token);
        Optional<UserEntity> empfaengerIDOptional = userService.getById(authorID);
        rejectIf(!isTokenValid(token) || !userID.isPresent() || !empfaengerIDOptional.isPresent());

        List<MessageEntity> messagesFrom = chatService.getMessagesFrom(authorID, userID.get());
        Collections.sort(messagesFrom);

        return successAnswerWithObject(messagesFrom);
    }

    @PostMapping("/getPartner")
    @Override
    public String getPartner(String token)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        List<ChatTO> partner = chatService.getPartner(userID.get());
        return successAnswerWithObject(partner);
    }

    @PostMapping("/markAsUnread")
    @Override
    public String markAsUnread(String token, String authorID)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        chatService.markAsRead(userID.get(), authorID);
        return simpleSuccessAnswer();
    }

    @PostMapping("/check")
    @Override
    public String checkForNewMessages(String token, String dateAsString)
    {
        Optional<String> userID = getUserIDFromToken(token);
        rejectIf(!isTokenValid(token) || !userID.isPresent());

        LocalDateTime date = getDateFromString(dateAsString);
        Optional<Long> count = chatService.checkForNewMessages(userID.get(), date);

        if (count.isPresent())
        {
            return successAnswerWithString(String.valueOf(count.get()));
        }
        return failureAnswer(SOME);
    }

    private LocalDateTime getDateFromString(String date)
    {
        //input: 2021-08-31 14:47
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, formatter);
    }


}
