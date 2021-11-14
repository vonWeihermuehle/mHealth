package net.mbmedia.mHealth.backend.ort;

import net.mbmedia.mHealth.backend.ort.impl.OrtEntity;

import java.util.List;
import java.util.Optional;

public interface IOrteService
{
    Optional<Long> add(OrtEntity entity);

    List<OrtEntity> getAll();

    List<OrtEntity> getAllFuer(String uuid);

    void removeByID(Long id);

    void update(OrtEntity entity);

    void deleteFor(String uuid);
}
