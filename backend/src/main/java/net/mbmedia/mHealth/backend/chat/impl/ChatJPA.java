package net.mbmedia.mHealth.backend.chat.impl;

import net.mbmedia.mHealth.backend.chat.IChatService;
import net.mbmedia.mHealth.backend.chat.impl.TO.ChatTO;
import net.mbmedia.mHealth.backend.db.BaseJPA;
import net.mbmedia.mHealth.backend.ort.impl.OrteJPA;
import net.mbmedia.mHealth.backend.user.impl.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Service
public class ChatJPA extends BaseJPA<MessageEntity> implements IChatService
{
    private static final Logger log = LoggerFactory.getLogger(OrteJPA.class);

    @Override
    public Optional<Long> add(MessageEntity message)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.persist(message);
            et.commit();
            return ofNullable(message.getId());
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

    @SuppressWarnings("unchecked")
    @Override
    public List<MessageEntity> getMessagesFrom(String authorID, String empfaengerID)
    {
        EntityManager em = getEntityManager();
        try
        {
            List<MessageEntity> resultList = em.createQuery("SELECT m from " + MessageEntity.class.getSimpleName() + " m" +
                            " where (m.authorID = :author and m.empfaengerID = :empfaenger)" +
                            " or (m.authorID = :empfaenger and m.empfaengerID = :author)" +
                            " order by m.erstellt desc")
                    .setParameter("author", authorID)
                    .setParameter("empfaenger", empfaengerID)
                    .getResultList();

            return resultList.stream().limit(100).collect(toList());
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
    public List<ChatTO> getPartner(String userID)
    {
        EntityManager em = getEntityManager();
        try
        {
            List<UserEntity> userEntities = em.createQuery("SELECT DISTINCT u from " + UserEntity.class.getSimpleName() + " u " +
                            "join " + MessageEntity.class.getSimpleName() + " m on (u.uuid = m.authorID or u.uuid = m.empfaengerID) " +
                            " where m.authorID = :userID or m.empfaengerID = :userID")
                    .setParameter("userID", userID)
                    .getResultList();

            return userEntities.stream()
                    .filter(user -> !user.getUuid().equals(userID))
                    .map(user ->
                    {
                        Object o = em.createQuery("SELECT COUNT(m) from " + MessageEntity.class.getSimpleName() + " m " +
                                        "where m.authorID = :andereID " +
                                        "and m.empfaengerID = :userID " +
                                        "and (m.gelesen = 0 or m.gelesen is null)")
                                .setParameter("andereID", user.getUuid())
                                .setParameter("userID", userID)
                                .getResultList().get(0);
                        Long count = (Long) o;
                        return new ChatTO(user.getFullName(), user.getUsername(), user.getUuid(), count > 0L);
                    })
                    .collect(toList());
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
    public void markAsRead(String empfaengerId, String authorID)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("UPDATE " + MessageEntity.class.getSimpleName() + " m set m.gelesen = 1 where m.authorID = :authorID and m.empfaengerID = :empfaengerID")
                    .setParameter("authorID", authorID)
                    .setParameter("empfaengerID", empfaengerId)
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
    public Optional<Long> checkForNewMessages(String empfaengerID, LocalDateTime date)
    {
        EntityManager em = getEntityManager();
        try
        {
            Long count = (Long) em.createQuery("SELECT COUNT(*) FROM " + MessageEntity.class.getSimpleName() + " m " +
                            "where m.empfaengerID = :empfaengerID " +
                            "and m.gelesen not in (1) " +
                            "and m.erstellt > :date")
                    .setParameter("empfaengerID", empfaengerID)
                    .setParameter("date", date)
                    .getResultList()
                    .get(0);
            return ofNullable(count);
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
    public void deleteAllMessagesFrom(String uuid)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.createQuery("DELETE from " + MessageEntity.class.getSimpleName() + " m " +
                            "where m.authorID = :uuid " +
                            "or m.empfaengerID = :uuid ")
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

    @SuppressWarnings("unchecked")
    public List<MessageEntity> getAll()
    {
        EntityManager em = getEntityManager();
        try
        {
            return (List<MessageEntity>) em.createQuery("SELECT m from " + MessageEntity.class.getSimpleName() + " m")
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
