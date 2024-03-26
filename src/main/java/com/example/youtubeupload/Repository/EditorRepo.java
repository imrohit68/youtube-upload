package com.example.youtubeupload.Repository;

import com.example.youtubeupload.Entity.Editor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorRepo extends JpaRepository<Editor,String> {
}
