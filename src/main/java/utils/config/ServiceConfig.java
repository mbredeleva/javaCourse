package utils.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ServiceConfig extends Config {

    @Key("login_url")
    String loginUrl();

    @Key("api_url")
    String apiUrl();

    @Key("client_id")
    String clientId();
}