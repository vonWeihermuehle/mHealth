package net.mbmedia.mHealth.backend.kontakt.impl;

import net.mbmedia.mHealth.backend.db.BaseJPA;
import net.mbmedia.mHealth.backend.kontakt.IKontakteService;
import net.mbmedia.mHealth.backend.unterstuetzung.impl.UnterstuetzungJPA;
import net.mbmedia.mHealth.backend.user.impl.TherapeutPatientEntity;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;

@Service
public class KontakteJPA extends BaseJPA<KontaktEntity> implements IKontakteService
{

    private static final Logger log = LoggerFactory.getLogger(UnterstuetzungJPA.class);

    @Override
    public Optional<Long> addKontakt(KontaktEntity kontakt)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.persist(kontakt);
            et.commit();
            return Optional.of(kontakt.getId());
        } catch (Exception e)
        {
            log.error(e.getMessage());
            et.rollback();
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public void delKontakt(Long id)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            KontaktEntity entity = em.find(KontaktEntity.class, id);
            em.remove(entity);
            et.commit();
        } catch (Exception e)
        {
            et.rollback();
            log.error(e.getMessage());
        } finally
        {
            closeEntitiyManager(em);
        }

    }

    @Override
    public void updateKontakt(KontaktEntity kontakt)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            KontaktEntity entity = em.find(KontaktEntity.class, kontakt.getId());
            entity.setArt(kontakt.getArt());
            entity.setEmail(kontakt.getEmail());
            entity.setName(kontakt.getName());
            entity.setPhone(kontakt.getPhone());
            et.commit();
        } catch (Exception e)
        {
            log.error(e.getMessage());
            et.rollback();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KontaktEntity> getAll()
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.createQuery("SELECT k from " + KontaktEntity.class.getSimpleName() + " k").getResultList();
        } catch (Exception e)
        {
            log.error(e.getMessage());
            return emptyList();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KontaktEntity> getAllFuer(String patientUUID)
    {
        EntityManager em = getEntityManager();
        try
        {
            List<KontaktEntity> kontakte = em.createQuery("SELECT k from " + KontaktEntity.class.getSimpleName() + " k where k.userID = :uuid")
                    .setParameter("uuid", patientUUID)
                    .getResultList();
            List<UserEntity> therapeuten = em.createQuery("SELECT u from " + UserEntity.class.getSimpleName() + " u " +
                            "join " + TherapeutPatientEntity.class.getSimpleName() + " t on t.id.therapeutUUID = u.uuid " +
                            "where t.id.patientUUID = :patientUUID")
                    .setParameter("patientUUID", patientUUID)
                    .getResultList();

            List<KontaktEntity> kontaktListeMitTherapeut = therapeuten.stream()
                    .map(u -> new KontaktEntity(0L, u.getFullName(), "Therapeut", u.getEmail(), "", u.getUuid()))
                    .collect(Collectors.toList());

            kontaktListeMitTherapeut.addAll(kontakte);
            return kontaktListeMitTherapeut;
        } catch (Exception e)
        {
            log.error(e.getMessage());
            return emptyList();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public Optional<KontaktEntity> getByID(Long id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return Optional.of(em.find(KontaktEntity.class, id));
        } catch (Exception e)
        {
            log.error(e.getMessage());
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public void deleteKontakteFor(String uuid)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + KontaktEntity.class.getSimpleName() + " k " +
                            "where k.userID = :uuid")
                    .setParameter("uuid", uuid)
                    .executeUpdate();
            et.commit();
        } catch (Exception e)
        {
            log.error(e.getMessage());
        } finally
        {
            closeEntitiyManager(em);
        }
    }
}
