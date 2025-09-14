package dev.louisa.jam.hub.infrastructure.mail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailProperties {

    private String user;
    private String password;
    private Smtp smtp = new Smtp();

    @Getter
    @Setter
    public static class Smtp {
        private String host;
        private String port;
        private String auth;
        private Starttls starttls = new Starttls();
        private Ssl ssl = new Ssl();
    }

    @Getter
    @Setter
    public static class Starttls {
        private String enable;
    }

    @Getter
    @Setter
    public static class Ssl {
        private String protocol;
    }
}