package net.mbmedia.mHealth.backend.param.impl;

import net.mbmedia.mHealth.backend.db.BaseJPA;
import net.mbmedia.mHealth.backend.ort.impl.OrteJPA;
import net.mbmedia.mHealth.backend.param.IParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Optional;

import static java.util.Optional.empty;

@Service
public class ParameterJPA extends BaseJPA<ParameterEntity> implements IParameterService
{

    private static final Logger log = LoggerFactory.getLogger(OrteJPA.class);

    @Override
    public Optional<Integer> getIntParam(String param)
    {
        EntityManager em = getEntityManager();
        try
        {
            return Optional.of(em.find(ParameterEntity.class, param).getIntegerValue());
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
    public Optional<Boolean> getBoolParam(String param)
    {
        EntityManager em = getEntityManager();
        try
        {
            return Optional.of(em.find(ParameterEntity.class, param).getBooleanValue());
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
    public Optional<String> getStringParam(String param)
    {
        EntityManager em = getEntityManager();
        try
        {
            return Optional.of(em.find(ParameterEntity.class, param).getStringValue());
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
    public void setParam(String param, Object value)
    {
        EntityManager em = getEntityManager();
        Optional<ParameterEntity> oldParameter = getOldParameter(em, param);

        EntityTransaction et = startTransaction(em);
        if (oldParameter.isPresent())
        {
            ParameterEntity entity = oldParameter.get();
            entity.setValue(value);
            et.commit();
        } else
        {
            ParameterEntity entity = new ParameterEntity(param, value);
            em.persist(entity);
            et.commit();
        }
        closeEntitiyManager(em);
    }

    @Override
    public void persist(ParameterEntity entity)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = startTransaction(em);
        try
        {
            em.persist(entity);
            et.commit();
        } catch (Exception e)
        {
            et.rollback();
        } finally
        {
            closeEntitiyManager(em);
        }
    }

    private Optional<ParameterEntity> getOldParameter(EntityManager em, String param)
    {
        try
        {
            return Optional.of(em.find(ParameterEntity.class, param));
        } catch (Exception e)
        {
            //not found Exception
            return empty();
        }
    }
}
