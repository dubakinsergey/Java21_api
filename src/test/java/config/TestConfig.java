package config;

import org.aeonbits.owner.ConfigFactory;

public class TestConfig {

    public static final ProjectConfig CONFIG = ConfigFactory.create(ProjectConfig.class);

    public static String getBaseUrl() {
        return CONFIG.baseUrl();
    }

    public static int getTimeout() {
        return CONFIG.timeout();
    }
}