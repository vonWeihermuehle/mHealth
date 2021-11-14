package net.mbmedia.mHealth.backend.mail;

import net.mbmedia.mHealth.backend.user.impl.UserEntity;

public interface IMailService
{
    void sendRegisterConfirmation(String host, UserEntity to, String passwort);

    void sendNewPasswort(String host, UserEntity to, String passwort);

    void sendSchwellWertUeberschritten(UserEntity patient, UserEntity therapeut, int wert);
}
