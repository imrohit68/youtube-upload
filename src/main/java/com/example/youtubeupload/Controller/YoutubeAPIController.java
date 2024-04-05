package com.example.youtubeupload.Controller;

import com.example.youtubeupload.Entity.ChannelOwner;
import com.example.youtubeupload.Payloads.SuccessResponse;
import com.example.youtubeupload.Payloads.UploadVideoRequest;
import com.example.youtubeupload.Payloads.UserInfo;
import com.example.youtubeupload.Service.EditorService;
import com.example.youtubeupload.Service.OAuthFlowInitiation;
import com.example.youtubeupload.Service.TokenExchange;
import com.example.youtubeupload.Service.YoutubeAPIService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class YoutubeAPIController {


    private final OAuthFlowInitiation oAuthFlowInitiation;

    private final TokenExchange tokenExchange;
    private final YoutubeAPIService youtubeAPI;
    private final EditorService editorService;

    @GetMapping("/getAccess/{scope}")
    public void initiateOAuth2Flow(HttpServletResponse response, @PathVariable String scope) throws Exception {
        String authorizationUrl = oAuthFlowInitiation.getAuthorizationUrl(scope);
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<UserInfo> handleRedirect(@RequestParam("code") String code) throws Exception {
        return new ResponseEntity<>
                (tokenExchange.exchangeCodeForTokens(code),
                        HttpStatus.OK);
    }

    @PostMapping("/upload/{email}")
    @PreAuthorize("hasRole('EDITOR')")
    public ResponseEntity<SuccessResponse> uploadVideo(@RequestBody UploadVideoRequest uploadVideoRequest,@RequestParam("file") MultipartFile file,@RequestParam("image") MultipartFile image) throws Exception {
        List<ChannelOwner> owners = editorService.getChannelByEditor(uploadVideoRequest.getEditorEmail());
        for (ChannelOwner o : owners ){
            if(o.getEmail().equals(uploadVideoRequest.getOwnerEmail())){
                SuccessResponse successResponse = youtubeAPI.upload(file,uploadVideoRequest.getOwnerEmail(),uploadVideoRequest.getTitle(),uploadVideoRequest.getDescription(),uploadVideoRequest.getTags(),image);
                return new ResponseEntity<>(successResponse,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new SuccessResponse("Upload Failed",false),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
