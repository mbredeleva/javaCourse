package dataStructures;

import lombok.Data;

@Data
public class Credentials {
    String login;
    String password;

    public Credentials(String login, String password){
        this.login = login;
        this.password = password;
    }
}
