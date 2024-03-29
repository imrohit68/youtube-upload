package com.example.youtubeupload;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class YoutubeUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutubeUploadApplication.class, args);
    }

}
