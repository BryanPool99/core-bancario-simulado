package com.bryandev.notification_service.strategy.impl;

import com.bryandev.notification_service.model.events.TransferCompletedEvent;
import com.bryandev.notification_service.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationStrategy implements NotificationStrategy {

    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(TransferCompletedEvent event) {
        log.info("[EMAIL SERVICE] Enviando correo electrónico...");
        /*
        log.info("   -> Cuenta Origen: {}", event.getSourceAccountNumber());
        log.info("   -> Asunto: Transferencia Exitosa #{}", event.getTransferId());
        log.info("   -> Monto transferido: ${}", event.getAmount());
         */
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@corebancario.com");
            message.setTo("bryanpool991@hotmail.com"); // Aquí puedes mapear el correo real si viene en el evento
            message.setSubject("Transferencia Exitosa #" + event.getTransferId());
            message.setText(String.format(
                    "¡Hola!\n\n" +
                    "Tu transferencia ha sido completada exitosamente.\n\n" +
                    "Detalles de la operación:\n" +
                    "- ID de Transferencia: %d\n" +
                    "- Cuenta Origen: %s\n" +
                    "- Cuenta Destino: %s\n" +
                    "- Monto: $%s\n" +
                    "- Fecha y Hora: %s\n\n" +
                    "Gracias por usar nuestro servicio bancario.",
                    event.getTransferId(),
                    event.getSourceAccountNumber(),
                    event.getTargetAccountNumber(),
                    event.getAmount(),
                    event.getTimestamp()
            ));

            mailSender.send(message);
            log.info("✅ Correo electrónico enviado exitosamente para la transferencia ID: {}", event.getTransferId());
        } catch (Exception e) {
            log.error("Error al enviar el correo electrónico para la transferencia ID: {}", event.getTransferId(), e);
            throw new RuntimeException("Fallo en el envío de correo", e);
        }
    }

    @Override
    public String getType() {
        return "EMAIL";
    }
}
