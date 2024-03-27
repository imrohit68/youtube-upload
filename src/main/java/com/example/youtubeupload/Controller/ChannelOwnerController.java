package com.example.youtubeupload.Controller;

import com.example.youtubeupload.Payloads.AddEditorRequest;
import com.example.youtubeupload.Payloads.SuccessResponse;
import com.example.youtubeupload.Service.ChannelOwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChannelOwnerController {
    private final ChannelOwnerService channelOwnerService;
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/addEditor")
    public ResponseEntity<SuccessResponse> addEditor(@RequestBody AddEditorRequest addEditorRequest){
        return new ResponseEntity<>(
                channelOwnerService.addEditor(addEditorRequest.getEditorEmail(),addEditorRequest.getOwnerEmail()),
                HttpStatus.OK);
    }
}
