package net.mbmedia.mHealth.backend.user.impl;

import net.mbmedia.mHealth.backend.db.BaseJPA;
import net.mbmedia.mHealth.backend.user.ITherapeutPatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;

@Service
public class TherapeutPatientJPA extends BaseJPA<TherapeutPatientEntity> implements ITherapeutPatientService
{

    private static final Logger log = LoggerFactory.getLogger(TherapeutPatientJPA.class);

    @Override
    public void add(TherapeutPatientEntity entity)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);

        try
        {
            em.persist(entity);
            et.commit();
        } catch (Exception e)
        {
            log.error(e.getMessage());
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public void delete(TherapeutPatientEntity entity)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);

        try
        {
            TherapeutPatientEntity e = em.find(TherapeutPatientEntity.class, entity.toPrimaryKey());
            em.remove(e);
            et.commit();
        } catch (Exception e)
        {
            log.error(e.getMessage());
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TherapeutPatientEntity> getAll()
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.createQuery("SELECT e from " + TherapeutPatientEntity.class.getSimpleName() + " e")
                    .getResultList();
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
    public List<UserEntity> getPatientenFuer(String uuid)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.createNamedQuery("findFor").setParameter("uuid", uuid)
                    .getResultList();
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
    public void deleteForPatient(String patientUUID)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + TherapeutPatientEntity.class.getSimpleName() + " t " +
                            "where t.id.patientUUID = :uuid")
                    .setParameter("uuid", patientUUID)
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

    @Override
    public Optional<UserEntity> getTherapeutFor(String uuid)
    {
        EntityManager em = getEntityManager();
        try
        {
            String therapeutUUID = (String) em.createQuery("SELECT t.id.therapeutUUID from " + TherapeutPatientEntity.class.getSimpleName() + " t " +
                            "where t.id.patientUUID = :uuid")
                    .setParameter("uuid", uuid)
                    .getResultList()
                    .get(0);

            return Optional.of(em.find(UserEntity.class, therapeutUUID));
        } catch (Exception e)
        {
            log.error(e.getMessage());
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }
}
