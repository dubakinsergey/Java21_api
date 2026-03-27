package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ProjectConfig extends Config {

    @Key("base.url")
    @DefaultValue("https://jsonplaceholder.typicode.com")
    String baseUrl();

    @Key("timeout")
    @DefaultValue("5000")
    int timeout();
}