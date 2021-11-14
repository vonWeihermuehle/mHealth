package net.mbmedia.mHealth.backend.ort.impl;

import net.mbmedia.mHealth.backend.db.BaseJPA;
import net.mbmedia.mHealth.backend.ort.IOrteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class OrteJPA extends BaseJPA<OrtEntity> implements IOrteService
{

    private static final Logger log = LoggerFactory.getLogger(OrteJPA.class);

    @Override
    public Optional<Long> add(OrtEntity entity)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.persist(entity);
            et.commit();
            return Optional.of(entity.getId());
        } catch (Exception e)
        {
            log.error(e.getMessage());
            return Optional.empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrtEntity> getAll()
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<OrtEntity>) em.createQuery("SELECT o from " + OrtEntity.class.getSimpleName() + " o")
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
    public List<OrtEntity> getAllFuer(String uuid)
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<OrtEntity>) em.createQuery("SELECT o from " + OrtEntity.class.getSimpleName() + " o where o.patientUUID = :uuid")
                    .setParameter("uuid", uuid)
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
    public void removeByID(Long id)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            OrtEntity ortEntity = em.find(OrtEntity.class, id);
            em.remove(ortEntity);
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
    public void update(OrtEntity entity)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            OrtEntity ortEntity = em.find(OrtEntity.class, entity.getId());
            ortEntity.setTitel(entity.getTitel());
            ortEntity.setBeschreibung(entity.getBeschreibung());
            ortEntity.setLat(entity.getLat());
            ortEntity.setLng(entity.getLng());
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

    @Override
    public void deleteFor(String uuid)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + OrtEntity.class.getSimpleName() + " o " +
                            "where o.patientUUID = :uuid")
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
