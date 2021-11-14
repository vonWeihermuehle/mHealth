package net.mbmedia.mHealth.backend.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface IUnterstuetzungController
{
    String addUebung(
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "titel") String titel,
            @RequestParam(name = "text") String text,
            @RequestParam(name = "empfaenger") String empfaenger
    );

    String updateUebung(@RequestHeader(name = "token") String token,
                        @RequestParam(name = "uebungID") Long uebungID,
                        @RequestParam(name = "titel") String titel,
                        @RequestParam(name = "text") String text);


    String getUebungen(
            @RequestHeader(name = "token") String token
    );

    String getUebungenFuerPatient(
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "uuid") String uuid
    );

    String delUebung(
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "uebungID") Long uebungID
    );


}
