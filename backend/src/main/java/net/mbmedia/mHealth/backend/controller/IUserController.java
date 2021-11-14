package net.mbmedia.mHealth.backend.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


public interface IUserController
{
    String login(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "passwort") String passwort
    );

    String resetPassword(
            @RequestHeader(name="Host", required=false) final String host,
            @RequestParam(value = "email") String email
    );

    String getUserData(@RequestHeader(value = "token") String token);

    String addPatient(
            @RequestHeader(name="Host", required=false) final String host,
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "vorname") String vorname,
            @RequestParam(value = "nachname") String nachname,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "schwellwert") Integer schwellwert
    );

    String addTherapeut(
            @RequestHeader(name="Host", required=false) final String host,
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "vorname") String vorname,
            @RequestParam(value = "nachname") String nachname,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "username") String username
    );

    String showPatienten(@RequestHeader(value = "token") String token);

    String showPatientenNearby(@RequestHeader(value = "token") String token);

    String setLastCoordinates(@RequestHeader(value = "token") String token,
                              @RequestParam(value = "lat", required = false) Double lat,
                              @RequestParam(value = "lng", required = false) Double lng);

    String setSchwellwert(@RequestHeader(value = "token") String token,
                          @RequestParam(value = "userID") String userID,
                          @RequestParam(value = "wert") Integer wert);

    String removeOwnUserWithAllData(@RequestHeader(value = "token") String token);

    String changePassword(@RequestHeader(value = "token") String token,
                          @RequestParam(value = "altesPasswort") String altesPasswort,
                          @RequestParam(value = "neuesPasswort") String neuesPasswort);

}
