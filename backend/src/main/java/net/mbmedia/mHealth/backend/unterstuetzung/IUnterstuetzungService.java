package net.mbmedia.mHealth.backend.unterstuetzung;

import net.mbmedia.mHealth.backend.unterstuetzung.impl.UnterstuetzungEntity;

import java.util.List;
import java.util.Optional;

public interface IUnterstuetzungService
{
    Optional<Long> add(UnterstuetzungEntity entity);

    List<UnterstuetzungEntity> getAll();

    List<UnterstuetzungEntity> getAllFuer(String uuid);

    void removeByID(Long id);

    Optional<UnterstuetzungEntity> getByIDAndErstelltVon(long id, String therapeut);

    void update(long id, String titel, String text);

    void deleteForPatient(String patientUUID);
}
