package com.example.youtubeupload.Controller;

import com.example.youtubeupload.Entity.ChannelOwner;
import com.example.youtubeupload.Payloads.AddEditorRequest;
import com.example.youtubeupload.Payloads.SuccessResponse;
import com.example.youtubeupload.Service.ChannelOwnerService;
import com.example.youtubeupload.Service.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EditorController {
    private final EditorService editorService;
    private final ChannelOwnerService channelOwnerService;
    @PreAuthorize("hasRole('EDITOR')")
    @GetMapping("getOwners/{email}")
    public ResponseEntity<List<ChannelOwner>> getOwnerByEditor(@PathVariable String email){
        return new ResponseEntity<>(
                editorService.getChannelByEditor(email),
                HttpStatus.OK
        );
    }
    @PreAuthorize("hasRole('EDITOR')")
    @DeleteMapping("/deleteOwner")
    public ResponseEntity<SuccessResponse> deleteOwner(@RequestBody AddEditorRequest addEditorRequest){
        return new ResponseEntity<>(
               channelOwnerService.deleteEditor(addEditorRequest.getOwnerEmail(),addEditorRequest.getEditorEmail()),
               HttpStatus.OK
        );
    }

}
