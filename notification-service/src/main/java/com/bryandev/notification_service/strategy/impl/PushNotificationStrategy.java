package com.bryandev.notification_service.strategy.impl;

import com.bryandev.notification_service.model.events.TransferCompletedEvent;
import com.bryandev.notification_service.strategy.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PushNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendNotification(TransferCompletedEvent event) {
        log.info("[PUSH NOTIFICATION] Enviando alerta al dispositivo móvil...");
        log.info("   -> ¡Tu transferencia de ${} hacia la cuenta {} fue exitosa!",
                event.getAmount(), event.getTargetAccountNumber());
    }

    @Override
    public String getType() {
        return "PUSH";
    }
}
