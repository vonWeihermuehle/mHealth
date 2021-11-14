package net.mbmedia.mHealth.backend.param.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static net.mbmedia.mHealth.backend.param.impl.ParameterEntity.TABLE_NAME;

@Entity
@Table(name = TABLE_NAME)
public class ParameterEntity
{
    public static final String TABLE_NAME = "param";

    @Id
    @Column(name = "param")
    private String param;

    @Column(name = "integer_value")
    private Integer integerValue;

    @Column(name = "boolean_value")
    private Boolean booleanValue;

    @Column(name = "string_value")
    private String stringValue;

    public ParameterEntity()
    {
    }

    public ParameterEntity(String param, Object value)
    {
        this.param = param;
        setValue(value);
    }

    public ParameterEntity(String param, Integer integerValue, Boolean booleanValue, String stringValue)
    {
        this.param = param;
        this.integerValue = integerValue;
        this.booleanValue = booleanValue;
        this.stringValue = stringValue;
    }

    public ParameterEntity(String param, Integer integerValue)
    {
        this.param = param;
        this.integerValue = integerValue;
    }

    public ParameterEntity(String param, Boolean booleanValue)
    {
        this.param = param;
        this.booleanValue = booleanValue;
    }

    public ParameterEntity(String param, String stringValue)
    {
        this.param = param;
        this.stringValue = stringValue;
    }

    public String getParam()
    {
        return param;
    }

    void setParam(String param)
    {
        this.param = param;
    }

    public Integer getIntegerValue()
    {
        return integerValue;
    }

    void setIntegerValue(Integer integerValue)
    {
        this.integerValue = integerValue;
    }

    public Boolean getBooleanValue()
    {
        return booleanValue;
    }

    void setBooleanValue(Boolean booleanValue)
    {
        this.booleanValue = booleanValue;
    }

    public String getStringValue()
    {
        return stringValue;
    }

    void setStringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }

    void setValue(Object value)
    {
        if(value instanceof Integer)
        {
            this.setIntegerValue((Integer) value);
        }
        if(value instanceof Boolean)
        {
            this.setBooleanValue((Boolean) value);
        }
        if(value instanceof String)
        {
            this.setStringValue((String) value);
        }
    }
}
