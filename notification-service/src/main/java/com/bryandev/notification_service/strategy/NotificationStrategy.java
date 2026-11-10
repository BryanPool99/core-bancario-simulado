package com.bryandev.notification_service.strategy;

import com.bryandev.notification_service.model.events.TransferCompletedEvent;

public interface NotificationStrategy {
    void sendNotification(TransferCompletedEvent event);
    String getType();
}
