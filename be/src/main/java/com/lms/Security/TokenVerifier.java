package com.lms.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lms.service.ConfigurationService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;


@Component
public class TokenVerifier {
    private final ConfigurationService configurationService;
    private static String GOOGLE_CLIENT_ID;

    public TokenVerifier(ConfigurationService configurationService) {
        this.configurationService = configurationService;
        GOOGLE_CLIENT_ID = configurationService.getGoogleClientId();
    }


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

    static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static String createJwt(String email, String userId) {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTime = currentTimeMillis + (24 * 60 * 60 * 1000);
        String jwt = Jwts.builder()
                .setSubject(email)
                .setId(userId)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(expirationTime))
                .signWith(secretKey)
                .compact();
        return jwt;
    }

    public boolean verifyJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();
            if (expirationDate.before(currentDate)) {
                throw new ExpiredJwtException(null, null, "JWT Token has expired");
            }
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }
}
