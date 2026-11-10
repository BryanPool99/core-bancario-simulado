package com.bryandev.notification_service.strategy.impl;

import com.bryandev.notification_service.model.events.TransferCompletedEvent;
import com.bryandev.notification_service.strategy.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendNotification(TransferCompletedEvent event) {
        log.info("[EMAIL SERVICE] Enviando correo electrónico...");
        log.info("   -> Cuenta Origen: {}", event.getSourceAccountNumber());
        log.info("   -> Asunto: Transferencia Exitosa #{}", event.getTransferId());
        log.info("   -> Monto transferido: ${}", event.getAmount());
    }

    @Override
    public String getType() {
        return "EMAIL";
    }
}
