package net.mbmedia.mHealth.backend.param;

import net.mbmedia.mHealth.backend.param.impl.ParameterEntity;

import java.util.Optional;

public interface IParameterService
{
    Optional<Integer> getIntParam(String param);

    Optional<Boolean> getBoolParam(String param);

    Optional<String> getStringParam(String param);

    void setParam(String param, Object value);

    void persist(ParameterEntity entity);
}