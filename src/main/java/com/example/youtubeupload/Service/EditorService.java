package com.example.youtubeupload.Service;

import com.example.youtubeupload.Entity.ChannelOwner;
import com.example.youtubeupload.Entity.Editor;
import com.example.youtubeupload.Exceptions.ResourceNotFoundException;
import com.example.youtubeupload.Payloads.SuccessResponse;
import com.example.youtubeupload.Repository.ChannelOwnerRepo;
import com.example.youtubeupload.Repository.EditorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditorService {
    private final EditorRepo editorRepo;
    private final ChannelOwnerRepo channelOwnerRepo;
    public List<ChannelOwner> getChannelByEditor(String email){
        Editor editor = editorRepo.findById(email).orElseThrow(()->new ResourceNotFoundException("Editor","email",email));
        return editor.getChannelOwners();
    }
 }
