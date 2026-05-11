package com.wtc.firebase;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FirebaseMessagingService {

    public void sendToToken(String fcmToken, String title, String body, Map<String, String> data) {
        if (fcmToken == null || fcmToken.isBlank()) return;
        try {
            Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .putAllData(data != null ? data : Map.of())
                .setAndroidConfig(AndroidConfig.builder().setPriority(AndroidConfig.Priority.HIGH).build())
                .setApnsConfig(ApnsConfig.builder().setAps(Aps.builder().setSound("default").build()).build())
                .build();
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Push enviado: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Erro push para {}: {}", fcmToken, e.getMessage());
        }
    }

    public void sendToMultipleTokens(List<String> tokens, String title, String body, Map<String, String> data) {
        List<String> valid = tokens.stream().filter(t -> t != null && !t.isBlank()).toList();
        if (valid.isEmpty()) return;
        try {
            MulticastMessage msg = MulticastMessage.builder()
                .addAllTokens(valid)
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .putAllData(data != null ? data : Map.of())
                .setAndroidConfig(AndroidConfig.builder().setPriority(AndroidConfig.Priority.HIGH).build())
                .build();
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(msg);
            log.info("Multicast: {} ok, {} falha", response.getSuccessCount(), response.getFailureCount());
        } catch (FirebaseMessagingException e) {
            log.error("Erro multicast: {}", e.getMessage());
        }
    }
}
