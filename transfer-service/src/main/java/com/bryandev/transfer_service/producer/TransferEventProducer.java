package com.bryandev.transfer_service.producer;

import com.bryandev.transfer_service.model.events.TransferCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    // Inyectamos el tópico desde el application.yml
    @Value("${app.kafka.topic.transfer-completed}")
    private String transferCompletedTopic;

    public void sendTransferCompletedEvent(TransferCompletedEvent event) {
        log.info("Enviando evento de transferencia completada al tópico [{}] para el ID: {}",
                transferCompletedTopic, event.getTransferId());
        kafkaTemplate.send(transferCompletedTopic, event.getSourceAccountNumber(), event)
                .whenComplete((result, ex) -> {
                    if (ex==null) {
                        log.info("Evento enviado exitosamente a Kafka [Tópico: {}, Partición: {}, Offset: {}]",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Fallo al enviar el evento de transferencia a Kafka", ex);
                    }
                });
    }
}
