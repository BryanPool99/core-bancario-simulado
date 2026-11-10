package com.bryandev.notification_service.factory;

import com.bryandev.notification_service.strategy.NotificationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationFactory {
    private final Map<String, NotificationStrategy> strategyMap;

    public NotificationFactory(List<NotificationStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getType().toUpperCase(),
                        Function.identity()
                ));
    }

    public NotificationStrategy getStrategy(String type) {
        NotificationStrategy strategy = strategyMap.get(type.toUpperCase());
        if (strategy==null) {
            throw new IllegalArgumentException("Estrategia de notificación no soportada: " + type);
        }
        return strategy;
    }
}
