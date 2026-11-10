package com.bryandev.notification_service.consumer;

import com.bryandev.notification_service.factory.NotificationFactory;
import com.bryandev.notification_service.model.events.TransferCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {
    private final NotificationFactory notificationFactory;

    @KafkaListener(
            topics = "${app.kafka.topic.transfer-completed}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleTransferCompleted(@Payload TransferCompletedEvent event) {
        log.info("Evento de transferencia recibido en notification-service para ID: {}", event.getTransferId());
        try {
            // Usamos la Factory para invocar la estrategia de Correo
            notificationFactory.getStrategy("EMAIL").sendNotification(event);

            // Usamos la Factory para invocar la estrategia Push
            notificationFactory.getStrategy("PUSH").sendNotification(event);

        } catch (Exception e) {
            log.error("Error al procesar las notificaciones para la transferencia ID: {}", event.getTransferId(), e);
        }
    }
}
