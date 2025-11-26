package com.andreea.ticket_tracker;

import com.andreea.ticket_tracker.security.admin.AdminProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AdminProperties.class)
@SpringBootApplication
public class TicketTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketTrackerApplication.class, args);
	}
}
