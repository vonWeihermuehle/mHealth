package net.mbmedia.mHealth.backend.token;

import net.mbmedia.mHealth.backend.db.BaseJPA;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.*;

import static java.util.Optional.empty;
import static net.mbmedia.mHealth.backend.util.CryptoHelper.hash;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class TokenService extends BaseJPA<TokenEntity>
{
    private static final Logger log = getLogger(TokenService.class);

    private static final int EXPIRATION = 7 * 24 * 60 * 60; //7 Tage

    public void addToken(String token)
    {
        String hashed = hash(token);
        Optional<TokenEntity> tokenEntity = getTokenByHash(hashed);

        if (tokenEntity.isPresent())
        {
            log.error("token already added");
            return;
        }

        putTokenByHash(hashed);
    }

    public boolean isTokenExpired(String token)
    {
        Date now = new Date();

        Optional<Date> expireDate = getExpireDateByToken(token);
        return expireDate.map(date -> !now.before(date)).orElse(true);
    }

    private Optional<Date> getExpireDateByToken(String token)
    {
        String hash = hash(token);
        Optional<TokenEntity> tokenEntity = getTokenByHash(hash);

        if (!tokenEntity.isPresent())
        {
            log.error("Token not in Cache");
            return Optional.empty();
        } else
        {
            extendExpiration(hash);
            return Optional.of(tokenEntity.get().getExpireDate());
        }
    }

    private void putTokenByHash(String hash)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            TokenEntity tokenEntity = new TokenEntity(hash, calculateExpirationDate());
            em.persist(tokenEntity);
            et.commit();
        } catch (Exception e)
        {
            log.error("Cant add Token", e);
            et.rollback();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    private void extendExpiration(String hash)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            TokenEntity tokenEntity = em.getReference(TokenEntity.class, hash);
            tokenEntity.setExpireDate(calculateExpirationDate());
            em.merge(tokenEntity);
            et.commit();
        } catch (Exception e)
        {
            log.error("cant extend expiration ", e);
            et.rollback();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    @SuppressWarnings("unchecked")
    public void removeExpired()
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            List<TokenEntity> expired = (List<TokenEntity>) em.createQuery("SELECT t from " + TokenEntity.class.getSimpleName() + " t where t.expireDate < :time")
                    .setParameter("time", new Date())
                    .getResultList();

            for (TokenEntity t : expired)
            {
                em.remove(t);
            }

            et.commit();
        } catch (Exception e)
        {
            log.error("cant delete expired ", e);
            et.rollback();
        } finally
        {
            closeEntitiyManager(em);
        }
    }


    public Optional<TokenEntity> getTokenByHash(String hash)
    {
        EntityManager em = getEntityManager();
        try
        {
            TokenEntity e = em.find(TokenEntity.class, hash);
            return Optional.of(e);
        } catch (Exception e)
        {
            log.error("Cant find token");
            return empty();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    private Date calculateExpirationDate()
    {
        Calendar gcal = new GregorianCalendar();
        gcal.setTime(new Date());
        gcal.add(Calendar.SECOND, EXPIRATION);
        return gcal.getTime();
    }

}
