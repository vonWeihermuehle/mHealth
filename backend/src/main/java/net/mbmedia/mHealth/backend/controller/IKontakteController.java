package net.mbmedia.mHealth.backend.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public interface IKontakteController
{
    String add(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "art") String art,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "userID") String userID);

    String remove(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "id") Long id);

    String update(@RequestHeader(value = "token") String token,
                  @RequestParam(value = "id") Long id,
                  @RequestParam(value = "name") String name,
                  @RequestParam(value = "art") String art,
                  @RequestParam(value = "email") String email,
                  @RequestParam(value = "phone") String phone);

    String getAllFuer(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "userID") String userID);

    String getOwn(
            @RequestHeader(value = "token") String token);
}
