package com.example.youtubeupload;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "API DOCUMENTATION FOR YOUTUBE-UPLOAD APPLICATION",
                description = "YOUTUBE-UPLOAD CHANNEL MANAGEMENT DOCUMENTATION ",
                version = "v1",
                contact = @Contact(
                        name = " Rohit Singh",
                        email = "sirohit328@gmail.com"
                )
        )
)
public class YoutubeUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutubeUploadApplication.class, args);
    }

}
