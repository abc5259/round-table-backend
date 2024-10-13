package com.roundtable.roundtable.business.notification.converter;

import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationResponseConvertMapper {

    private final Map<NotificationType, NotificationResponseConverter> notificationResponseConverters;

    public NotificationResponseConvertMapper(final List<NotificationResponseConverter> converters) {
        notificationResponseConverters = new HashMap<>();
        converters.forEach(converter -> notificationResponseConverters.put(converter.getNotificationSupportType(), converter));
    }

    public NotificationResponse convert(Notification notification) {
        NotificationResponseConverter converter = Optional.ofNullable(
                        notificationResponseConverters.get(notification.getNotificationType()))
                .orElseThrow(() -> {
                    String message = "Could not find NotificationResponseConverter for notification type "
                            + notification.getNotificationType();
                    log.error(message);
                    return new IllegalArgumentException(message);
                });

        return converter.toNotificationResponse(notification);
    }
}
