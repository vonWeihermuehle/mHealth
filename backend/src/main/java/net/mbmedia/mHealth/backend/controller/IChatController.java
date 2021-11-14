package net.mbmedia.mHealth.backend.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface IChatController
{
    String add(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "nachricht") String nachricht,
            @RequestParam(value = "empfaengerID") String empfaengerID
    );

    String getMessagesFrom(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "authorID") String authorID
    );

    String getPartner(@RequestHeader(value = "token") String token);

    String markAsUnread(@RequestHeader(value = "token") String token,
                        @RequestParam(value = "authorID") String authorID);

    String checkForNewMessages(@RequestHeader(value = "token") String token,
                               @RequestParam(value = "datetime") String dateAsString);
}
