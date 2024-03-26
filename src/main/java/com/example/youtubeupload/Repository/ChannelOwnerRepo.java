package com.example.youtubeupload.Repository;

import com.example.youtubeupload.Entity.ChannelOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelOwnerRepo extends JpaRepository<ChannelOwner,String> {
}
