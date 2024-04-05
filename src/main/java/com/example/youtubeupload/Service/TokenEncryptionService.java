package com.example.youtubeupload.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Service
public class TokenEncryptionService {
    private static final String AES = "AES";
    @Value("${encryption.key}")
    private String ENCRYPTION_KEY;
    public String encryptToken(String token) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(ENCRYPTION_KEY);
        Key key = new SecretKeySpec(keyBytes, AES);
        System.out.println(ENCRYPTION_KEY);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedTokenBytes = cipher.doFinal(token.getBytes());
        return Base64.getEncoder().encodeToString(encryptedTokenBytes);
    }

    public  String decryptToken(String encryptedToken) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(ENCRYPTION_KEY);
        Key key = new SecretKeySpec(keyBytes, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedTokenBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedToken));
        return new String(decryptedTokenBytes);
    }
}
