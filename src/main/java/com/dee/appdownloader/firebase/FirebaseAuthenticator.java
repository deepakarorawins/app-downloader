package com.dee.appdownloader.firebase;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class FirebaseAuthenticator {
    public String getAccessToken() {
        try {
            String path = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            if (path == null || path.isEmpty()) path = "src/main/resources/google-cloud-lle.json";

            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(path))
                    .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-platform"));
            credentials.refresh();

            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException("Auth failed: " + e.getMessage(), e);
        }
    }
}

