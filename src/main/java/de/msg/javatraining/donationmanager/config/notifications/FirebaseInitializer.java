package de.msg.javatraining.donationmanager.config.notifications;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseInitializer {
    @Value("${app.firebase-config-file}")
    private String firebaseConfigPath;

    @Bean
    FirebaseMessaging firebaseMessaging(){
        try {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream());
            FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build();
            FirebaseApp  app = FirebaseApp.initializeApp(firebaseOptions,"donation-management");
            return FirebaseMessaging.getInstance(app);
        } catch (IOException e) {
            log.warn("Firebase initialization went wrong");
            throw new RuntimeException(e);
        }
    }
}
