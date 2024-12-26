package com.example.virtualwinesommelierbackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VirtualWineSommelierBackendApplication {

    public static void main(String[] args) {
        // Load environment variables from the .env file
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        // Disable AWS SDK v1.x deprecation announcement warning
        System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");

        SpringApplication.run(VirtualWineSommelierBackendApplication.class, args);
    }

}
