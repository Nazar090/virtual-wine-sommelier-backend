package com.example.virtualwinesommelierbackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VirtualWineSommelierBackendApplication {

    public static void main(String[] args) {
        // Load environment variables from the .env file
        Dotenv dotenv = Dotenv.configure().load();

        SpringApplication.run(VirtualWineSommelierBackendApplication.class, args);
    }

}
