package net.mbmedia.mHealth.backend.fragebogen;

import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenAbgeschlossenEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenEntity;
import net.mbmedia.mHealth.backend.fragebogen.impl.FragebogenZuweisungEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IFragebogenService
{
    Optional<Long> addFragebogen(FragebogenEntity entity);

    void delFragebogen(Long id);

    void updateFragebogen(FragebogenEntity updated);

    List<FragebogenEntity> getAll();

    List<FragebogenZuweisungEntity> getAllZuweisungen();

    List<FragebogenZuweisungEntity> getAllZuweisungenForFragebogenID(Long id);

    List<FragebogenZuweisungEntity> getAllCronZuweisungen();

    Optional<FragebogenEntity> getById(Long id);

    List<FragebogenEntity> getForAuthor(String authorID);

    List<FragebogenEntity> getForEmpfaenger(String empfaengerID);

    void removeForEmpfaenger(String empfaengerID);

    Optional<Long> addZuweisung(FragebogenZuweisungEntity entity);

    void delZuweisung(Long id);

    void delNonCronZuweisung(String empfaengerUUID, Long fragebogenId);

    void updateZuweisung(FragebogenZuweisungEntity updated);

    Optional<FragebogenZuweisungEntity> getZuweisungById(Long id);

    List<FragebogenZuweisungEntity> getForDate(LocalDate date);

    Optional<Long> addAbgeschlossen(FragebogenAbgeschlossenEntity entity);

    List<FragebogenAbgeschlossenEntity> getAllAbgeschlossen();

    List<FragebogenAbgeschlossenEntity> getAllAbgeschlossenFuerPatient(String uuid);

}
