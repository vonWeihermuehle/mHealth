package net.mbmedia.mHealth.backend.fragebogen.impl;

import net.mbmedia.mHealth.backend.db.BaseJPA;
import net.mbmedia.mHealth.backend.fragebogen.IFragebogenService;
import net.mbmedia.mHealth.backend.jobs.setCronZuweisungenJOB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;

@SuppressWarnings("unchecked")
@Service
public class FragebogenJPA extends BaseJPA<FragebogenEntity> implements IFragebogenService
{
    private static final Logger log = LoggerFactory.getLogger(FragebogenJPA.class);

    @Override
    public void removeAll()
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + FragebogenEntity.class.getSimpleName()).executeUpdate();
            em.createQuery("DELETE from " + FragebogenZuweisungEntity.class.getSimpleName()).executeUpdate();
            em.createQuery("DELETE from " + FragebogenAbgeschlossenEntity.class.getSimpleName()).executeUpdate();
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
    public Optional<Long> addFragebogen(FragebogenEntity entity)
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
            et.rollback();
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public void delFragebogen(Long id)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + FragebogenZuweisungEntity.class.getSimpleName() + " z " +
                    "where z.fragebogen.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            FragebogenEntity entity = em.find(FragebogenEntity.class, id);
            em.remove(entity);
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
    public void updateFragebogen(FragebogenEntity updated)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            FragebogenEntity entity = em.find(FragebogenEntity.class, updated.getId());
            entity.setTitel(updated.getTitel());
            entity.setBeschreibung(updated.getBeschreibung());
            entity.setJson(updated.getJson().getBytes(StandardCharsets.UTF_8));
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
    public List<FragebogenEntity> getAll()
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<FragebogenEntity>) em.createQuery("SELECT f from " + FragebogenEntity.class.getSimpleName() + " f")
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
    public List<FragebogenZuweisungEntity> getAllZuweisungen()
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<FragebogenZuweisungEntity>) em.createQuery("SELECT f from " + FragebogenZuweisungEntity.class.getSimpleName() + " f")
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
    public List<FragebogenZuweisungEntity> getAllZuweisungenForFragebogenID(Long id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<FragebogenZuweisungEntity>) em.createQuery("SELECT f from " + FragebogenZuweisungEntity.class.getSimpleName() + " f " +
                            "where f.fragebogen.id = :id")
                    .setParameter("id", id)
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
    public List<FragebogenZuweisungEntity> getAllCronZuweisungen()
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<FragebogenZuweisungEntity>) em.createQuery("SELECT f from " + FragebogenZuweisungEntity.class.getSimpleName() + " f " +
                            "where f.cron is not null")
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
    public Optional<FragebogenEntity> getById(Long id)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            return Optional.of(em.find(FragebogenEntity.class, id));
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
    public List<FragebogenEntity> getForAuthor(String authorID)
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<FragebogenEntity>) em.createQuery("SELECT f from " + FragebogenEntity.class.getSimpleName() + " f " +
                            "where f.author = :author")
                    .setParameter("author", authorID)
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
    public List<FragebogenEntity> getForEmpfaenger(String empfaengerID)
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<FragebogenEntity>) em.createQuery("SELECT z.fragebogen from " + FragebogenZuweisungEntity.class.getSimpleName() + " z " +
                            "where z.empfaenger.uuid = :uuid " +
                            "and z.cron is null or z.cron = ''")
                    .setParameter("uuid", empfaengerID)
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
    public void removeForEmpfaenger(String empfaengerID)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + FragebogenZuweisungEntity.class.getSimpleName() + " z " +
                            "where z.empfaenger.uuid = :uuid")
                    .setParameter("uuid", empfaengerID)
                    .executeUpdate();
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
    public Optional<Long> addZuweisung(FragebogenZuweisungEntity entity)
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
            et.rollback();
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public void delZuweisung(Long id)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            FragebogenZuweisungEntity entity = em.find(FragebogenZuweisungEntity.class, id);
            em.remove(entity);
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
    public void delNonCronZuweisung(String empfaengerUUID, Long fragebogenId)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + FragebogenZuweisungEntity.class.getSimpleName() + " z " +
                            "where z.empfaenger.uuid = :uuid " +
                            "and z.fragebogen.id = :fragebogenId " +
                            "and z.cron is null or z.cron = ''")
                    .setParameter("uuid", empfaengerUUID)
                    .setParameter("fragebogenId", fragebogenId)
                    .executeUpdate();
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
    public void updateZuweisung(FragebogenZuweisungEntity updated)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            FragebogenZuweisungEntity entity = em.find(FragebogenZuweisungEntity.class, updated.getId());
            entity.setFragebogen(updated.getFragebogen());
            entity.setCron(updated.getCron());
            entity.setEmpfaenger(updated.getEmpfaenger());
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
    public Optional<FragebogenZuweisungEntity> getZuweisungById(Long id)
    {
        EntityManager em = getEntityManager();
        try
        {
            FragebogenZuweisungEntity entity = em.find(FragebogenZuweisungEntity.class, id);
            return Optional.of(entity);
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
    public List<FragebogenZuweisungEntity> getForDate(LocalDate date)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.createQuery("SELECT f from " + FragebogenZuweisungEntity.class.getSimpleName() + " f " +
                            "where f.erstellt = :date")
                    .setParameter("date", date)
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
    public Optional<Long> addAbgeschlossen(FragebogenAbgeschlossenEntity entity)
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
            et.rollback();
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public List<FragebogenAbgeschlossenEntity> getAllAbgeschlossen()
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<FragebogenAbgeschlossenEntity>) em.createQuery("SELECT f from " + FragebogenAbgeschlossenEntity.class.getSimpleName() + " f")
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
    public List<FragebogenAbgeschlossenEntity> getAllAbgeschlossenFuerPatient(String uuid)
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<FragebogenAbgeschlossenEntity>) em.createQuery("SELECT f from " + FragebogenAbgeschlossenEntity.class.getSimpleName() + " f " +
                            "where f.patient.uuid = :uuid " +
                            "order by f.erstellt desc")
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

}
