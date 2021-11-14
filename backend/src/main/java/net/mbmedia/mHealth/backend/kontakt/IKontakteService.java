package net.mbmedia.mHealth.backend.kontakt;

import net.mbmedia.mHealth.backend.kontakt.impl.KontaktEntity;

import java.util.List;
import java.util.Optional;

public interface IKontakteService
{
    Optional<Long> addKontakt(KontaktEntity kontakt);

    void delKontakt(Long id);

    void updateKontakt(KontaktEntity kontakt);

    List<KontaktEntity> getAll();

    List<KontaktEntity> getAllFuer(String patientUUID);

    Optional<KontaktEntity> getByID(Long id);

    void deleteKontakteFor(String uuid);
}
