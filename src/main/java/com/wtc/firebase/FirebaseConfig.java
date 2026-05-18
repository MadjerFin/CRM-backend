package com.wtc.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.path:#{null}}")
    private Resource credentialsResource;

    @Value("${FIREBASE_CREDENTIALS_JSON:#{null}}")
    private String credentialsJson;

    @PostConstruct
    public void initialize() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            GoogleCredentials credentials;
            if (credentialsJson != null && !credentialsJson.isBlank()) {
                credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8)));
            } else if (credentialsResource != null && credentialsResource.exists()) {
                credentials = GoogleCredentials.fromStream(credentialsResource.getInputStream());
            } else {
                return;
            }
            FirebaseApp.initializeApp(FirebaseOptions.builder()
                .setCredentials(credentials).build());
        }
    }
}
