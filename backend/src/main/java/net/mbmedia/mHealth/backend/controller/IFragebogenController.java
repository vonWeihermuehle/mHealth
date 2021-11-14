package net.mbmedia.mHealth.backend.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface IFragebogenController
{
    String addFragebogen(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "titel") String titel,
            @RequestParam(value = "beschreibung") String beschreibung,
            @RequestParam(value = "json") String json
    );

    String updateFragebogen(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "titel") String titel,
            @RequestParam(value = "beschreibung") String beschreibung,
            @RequestParam(value = "json") String json
    );

    String delFragebogen(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "id") Long id
    );

    String getOwn(
            @RequestHeader(value = "token") String token
    );

    String getZuweisungenFuerFragebogen(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "id") Long id
    );

    String addZuweisung(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "empfaengerID") String empfaengerID,
            @RequestParam(value = "cron") String cron
    );

    String delZuweisung(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "id") Long id
    );

    String addAbgeschlossen(@RequestHeader(value = "token") String token,
                            @RequestParam(value = "ergebnis") String ergebnis,
                            @RequestParam(value = "id") Long id);

    String getAbgeschlossenFuer(@RequestHeader(value = "token") String token,
                                @RequestParam(value = "uuid") String uuid);
}
