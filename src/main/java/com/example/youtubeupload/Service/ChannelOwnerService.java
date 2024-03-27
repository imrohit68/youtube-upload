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
public class ChannelOwnerService {
    private final ChannelOwnerRepo channelOwnerRepo;
    private final EditorRepo editorRepo;
    public SuccessResponse addEditor(String editorEmail,String ownerEmail){
        ChannelOwner channelOwner = channelOwnerRepo.findById(ownerEmail).orElseThrow(()->new ResourceNotFoundException("Channel Owner","email",ownerEmail));
        List<Editor> editors = channelOwner.getEditors();
        Editor editor = editorRepo.findById(editorEmail).orElseThrow(()-> new ResourceNotFoundException("Editor","email",editorEmail));
        editors.add(editor);
        channelOwner.setEditors(editors);
        channelOwnerRepo.save(channelOwner);

        List<ChannelOwner> channelOwners = editor.getChannelOwners();
        channelOwners.add(channelOwner);
        editor.setChannelOwners(channelOwners);
        editorRepo.save(editor);

        return new SuccessResponse("Editor Added Successfully",true);
    }
}
