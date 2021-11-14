package net.mbmedia.mHealth.backend.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@org.springframework.context.annotation.Configuration
@PropertySource(value = { "classpath:application.properties" })
public class Configuration implements EnvironmentAware
{

    static Environment environment;


    public String load(String propertyName)
    {
        return environment.getProperty(propertyName);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void setEnvironment(Environment environment) {
        Configuration.environment = environment;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
