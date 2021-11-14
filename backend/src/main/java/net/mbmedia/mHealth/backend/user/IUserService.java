package net.mbmedia.mHealth.backend.user;

import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import net.mbmedia.mHealth.backend.util.StandortHelper;

import java.util.List;
import java.util.Optional;

public interface IUserService
{
    Optional<String> register(UserEntity user);

    List<UserEntity> login(String usename, String passwort);

    List<UserEntity> getAll();

    Optional<UserEntity> getById(String uuid);

    Optional<UserEntity> getByUsername(String username);

    Optional<UserEntity> getByEmail(String email);

    boolean isEmailAndUsernameUnused(String username, String email);

    List<UserEntity> getNearby(StandortHelper.UmkreisPunkte umkreisPunkte);

    void setLastCoordinates(String uuid, Double lat, Double lng);

    void setSchwellwert(String uuid, int schwellwert);

    boolean updatePassword(String uuid, String passwort);

    void deleteUserById(String uuid);
}
