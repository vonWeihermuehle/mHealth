package net.mbmedia.mHealth.backend.param.impl;

import net.mbmedia.mHealth.backend.param.IParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static net.mbmedia.mHealth.backend.TestVorbereiter.truncateAllTables;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParameterJPATest
{

    @Autowired
    private IParameterService parameterService;

    @BeforeEach
    public void beforeEach()
    {
        truncateAllTables(parameterService);
    }

    @Test
    public void setParameter()
    {
        int intValue = 1;
        String stringValue = "test";

        parameterService.setParam("int", intValue);
        parameterService.setParam("bool", true);
        parameterService.setParam("string", stringValue);

        assert(parameterService.getBoolParam("bool").get());
        assert(parameterService.getIntParam("int").get()).equals(intValue);
        assert(parameterService.getStringParam("string").get()).equals(stringValue);
    }


}