package com.andreea.ticket_tracker.security.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.admin")
public class AdminProperties {

    private String email;
    private String username;
    private String password;
}
