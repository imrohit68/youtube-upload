package com.example.youtubeupload.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChannelOwner {
    @Id
    private String email;
    private String channelId;
    private String channelName;
    private String refreshToken;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Editor> editors;
}
