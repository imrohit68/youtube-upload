package com.example.youtubeupload.Payloads;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class UploadVideoRequest {
    private String editorEmail;
    private String ownerEmail;
    private String title;
    private String description;
    private String tags;
}
