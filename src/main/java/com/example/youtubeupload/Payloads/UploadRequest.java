package com.example.youtubeupload.Payloads;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadRequest {
    private String email;
    private String tags;
    private String title;
    private String path;
}
