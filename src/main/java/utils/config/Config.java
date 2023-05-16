package utils.config;

import org.aeonbits.owner.ConfigFactory;

public class Config {
    private Config() {
        throw new IllegalStateException("Utility class");
    }

    public static ServiceConfig getServiceConfig() {
        return ConfigFactory.create(ServiceConfig.class);
    }

    public static UserConfig getUserConfig() {
        return ConfigFactory.create(UserConfig.class);
    }

}