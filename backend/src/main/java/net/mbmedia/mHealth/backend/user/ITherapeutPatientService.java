package net.mbmedia.mHealth.backend.user;

import net.mbmedia.mHealth.backend.user.impl.TherapeutPatientEntity;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;

import java.util.List;
import java.util.Optional;

public interface ITherapeutPatientService
{
    void add(TherapeutPatientEntity entity);

    void delete(TherapeutPatientEntity entity);

    List<TherapeutPatientEntity> getAll();

    List<UserEntity> getPatientenFuer(String uuid);

    void deleteForPatient(String patientUUID);

    Optional<UserEntity> getTherapeutFor(String uuid);
}
