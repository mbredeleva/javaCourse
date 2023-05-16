package utils.config;

import dataStructures.Credentials;
import org.aeonbits.owner.Config;

public interface UserConfig extends Config {

    Credentials user1 = new Credentials("api-user3@iwt.net","b3z0nV0cLO");
    Credentials user2 = new Credentials("api-user4@iwt.net","b3z0nV0cLO");
}
