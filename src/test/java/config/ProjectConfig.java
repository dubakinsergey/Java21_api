package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ProjectConfig extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("timeout")
    int timeout();
}