package com.example.lms.Controller;

import com.example.lms.Security.TokenVerifier;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
public class VerificationController {
    @PostMapping
    public ResponseEntity<String> verifyToken(@RequestBody String jwtToken) {
        JSONObject jsonObject = new JSONObject(jwtToken);
        ResponseEntity<String> response;
        ResponseEntity<String> verificationResult = TokenVerifier.verifyToken(jsonObject.getString("token"));

        if (verificationResult.getStatusCode() == HttpStatus.OK) {
            response = ResponseEntity.ok(verificationResult.getBody());
        } else {
            response = ResponseEntity.status(verificationResult.getStatusCode()).body(verificationResult.getBody());
        }
        return response;
    }
}
