package net.mbmedia.mHealth.backend.user.impl;

import net.mbmedia.mHealth.backend.db.BaseJPA;
import net.mbmedia.mHealth.backend.user.IUserService;
import net.mbmedia.mHealth.backend.util.StandortHelper;
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
public class UserJPA extends BaseJPA<UserEntity> implements IUserService
{
    private static final Logger log = LoggerFactory.getLogger(UserJPA.class);

    @Override
    public Optional<String> register(UserEntity user)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.persist(user);
            et.commit();
            return Optional.of(user.getUuid());
        } catch (Exception e)
        {
            log.error(e.getMessage());
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserEntity> login(String username, String passwort)
    {
        EntityManager em = getEntityManager();
        try{
            return em.createQuery("SELECT u from " + UserEntity.class.getSimpleName() + " u " +
                            "where u.username = :username " +
                            "and u.passwort = :passwort")
                    .setParameter("username", username)
                    .setParameter("passwort", passwort)
                    .getResultList();
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
    public List<UserEntity> getAll()
    {
        EntityManager em = getEntityManager();
        try{
            return em.createQuery("SELECT u from " + UserEntity.class.getSimpleName() + " u ")
                    .getResultList();
        }catch (Exception e)
        {
            log.error(e.getMessage());
            return emptyList();
        }finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public Optional<UserEntity> getById(String uuid)
    {
        EntityManager em = getEntityManager();
        try
        {
            return Optional.of(em.find(UserEntity.class, uuid));
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
    public Optional<UserEntity> getByUsername(String username)
    {
        EntityManager em = getEntityManager();
        try
        {
            UserEntity user = (UserEntity) em.createQuery("Select u from " + UserEntity.class.getSimpleName() + " u where u.username = :username")
                    .setParameter("username", username)
                    .getResultList()
                    .get(0);
            return Optional.of(user);
        } catch (Exception e)
        {
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public Optional<UserEntity> getByEmail(String email)
    {
        EntityManager em = getEntityManager();
        try
        {
            UserEntity user = (UserEntity) em.createQuery("Select u from " + UserEntity.class.getSimpleName() + " u where u.email = :email")
                    .setParameter("email", email)
                    .getResultList()
                    .get(0);
            return Optional.of(user);
        } catch (Exception e)
        {
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public boolean isEmailAndUsernameUnused(String username, String email)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.createQuery("SELECT u from " + UserEntity.class.getSimpleName() + " u " +
                            "where u.username = :username " +
                            "or u.email = :email")
                    .setParameter("username", username)
                    .setParameter("email", email)
                    .getResultList()
                    .isEmpty();
        } catch (Exception e)
        {
            log.error(e.getMessage());
            return false;
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserEntity> getNearby(StandortHelper.UmkreisPunkte umkreisPunkte)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.createQuery("SELECT u from " + UserEntity.class.getSimpleName() + " u " +
                            "where u.lat <= :maxLat " +
                            "and u.lat >= :minLat " +
                            "and u.lng <= :maxLng " +
                            "and u.lng >= :minLng " +
                            "and (u.therapeut = null or u.therapeut = 0)")
                    .setParameter("maxLat", umkreisPunkte.getMaxLat())
                    .setParameter("minLat", umkreisPunkte.getMinLat())
                    .setParameter("maxLng", umkreisPunkte.getMaxLng())
                    .setParameter("minLng", umkreisPunkte.getMinlng())
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
    public void setLastCoordinates(String uuid, Double lat, Double lng)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            UserEntity user = em.find(UserEntity.class, uuid);
            user.setLat(lat);
            user.setLng(lng);
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
    public void setSchwellwert(String uuid, int schwellwert)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            UserEntity user = em.find(UserEntity.class, uuid);
            user.setSchwellwert(schwellwert);
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
    public boolean updatePassword(String uuid, String passwort)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            UserEntity user = em.find(UserEntity.class, uuid);
            user.setPasswort(passwort);
            et.commit();
            return true;
        } catch (Exception e)
        {
            log.error(e.getMessage());
            et.rollback();
            return false;
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @Override
    public void deleteUserById(String uuid)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + UserEntity.class.getSimpleName() + " u " +
                            "where u.uuid = :uuid")
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
