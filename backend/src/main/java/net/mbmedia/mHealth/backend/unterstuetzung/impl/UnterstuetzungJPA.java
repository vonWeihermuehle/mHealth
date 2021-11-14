package net.mbmedia.mHealth.backend.unterstuetzung.impl;

import net.mbmedia.mHealth.backend.db.BaseJPA;
import net.mbmedia.mHealth.backend.unterstuetzung.IUnterstuetzungService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;

@SuppressWarnings("unchecked")
@Service
public class UnterstuetzungJPA extends BaseJPA<UnterstuetzungEntity> implements IUnterstuetzungService
{

    private static final Logger log = LoggerFactory.getLogger(UnterstuetzungJPA.class);

    @Override
    public Optional<Long> add(UnterstuetzungEntity entity)
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
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public List<UnterstuetzungEntity> getAll()
    {
        EntityManager em = getEntityManager();
        try{
            return em.createQuery("SELECT u from " + UnterstuetzungEntity.class.getSimpleName() + " u ").getResultList();
        }catch (Exception e)
        {
            log.error(e.getMessage());
            return emptyList();
        }finally
        {
            closeEntitiyManager(em);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UnterstuetzungEntity> getAllFuer(String uuid)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.createQuery("SELECT u from " + UnterstuetzungEntity.class.getSimpleName() + " u where u.empfaenger = :uuid")
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
            em.createQuery("DELETE from " + UnterstuetzungEntity.class.getSimpleName() + " u where u.id = :id")
                    .setParameter("id", id)
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
    public Optional<UnterstuetzungEntity> getByIDAndErstelltVon(long id, String therapeut)
    {
        EntityManager em = getEntityManager();
        try
        {
            UnterstuetzungEntity u = (UnterstuetzungEntity) em.createQuery("SELECT u from " + UnterstuetzungEntity.class.getSimpleName() + " u where u.id = :id and u.author = :author")
                    .setParameter("id", id)
                    .setParameter("author", therapeut)
                    .getResultList()
                    .get(0);

            return Optional.of(u);
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
    public void update(long id, String titel, String text)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            UnterstuetzungEntity unterstuetzungEntity = em.find(UnterstuetzungEntity.class, id);
            unterstuetzungEntity.setTitel(titel);
            unterstuetzungEntity.setText(text);
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
    public void deleteForPatient(String patientUUID)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + UnterstuetzungEntity.class.getSimpleName() + " u " +
                            "where u.empfaenger = :uuid")
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
}
