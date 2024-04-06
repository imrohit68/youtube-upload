package com.example.youtubeupload.Payloads;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class UploadRequest {
    private String email;
    private String tags;
    private String title;
    private String path;
}
