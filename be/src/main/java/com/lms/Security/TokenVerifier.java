package com.example.lms.Security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

public class TokenVerifier {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private static String GOOGLE_CLIENT_ID;


    public static ResponseEntity<String> verifyToken(String jwtToken) {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();
        try {
            GoogleIdToken idToken = verifier.verify(jwtToken);
            if (idToken != null) {
                String userId = idToken.getPayload().getSubject();
                String email = idToken.getPayload().getEmail();

                String newJwtToken = createJwt(email, userId);
                return ResponseEntity.ok(newJwtToken);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (GeneralSecurityException | IOException exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    private static String createJwt(String email, String userId) {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTime = currentTimeMillis + (60 * 60 * 1000);
        String jwt = Jwts.builder()
                .setSubject(email)
                .setId(userId)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(expirationTime))
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
                .compact();
        return jwt;
    }
}
