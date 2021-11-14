package net.mbmedia.mHealth.backend.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface IOrteController
{
    String addOrt(
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "titel") String titel,
            @RequestParam(name = "beschreibung") String beschreibung,
            @RequestParam(name = "patient") String patient,
            @RequestParam(name = "lat") String lat,
            @RequestParam(name = "lng") String lng
    );

    String updateOrt(@RequestHeader(name = "token") String token,
                    @RequestParam(name = "ortID") Long ortID,
                     @RequestParam(name = "titel") String titel,
                     @RequestParam(name = "beschreibung") String beschreibung,
                     @RequestParam(name = "lat") String lat,
                     @RequestParam(name = "lng") String lng);


    String getOrteFuerPatient(
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "uuid") String uuid
    );

    String delOrt(
            @RequestHeader(name = "token") String token,
            @RequestParam(name = "ortID") Long ortID
    );
}
