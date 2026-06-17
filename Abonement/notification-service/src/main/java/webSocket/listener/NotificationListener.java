package webSocket.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import webSocket.dto.EventMetadata;
import webSocket.dto.NotificationMessage;
import webSocket.handler.NotificationWebSocketHandler;


import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class NotificationListener {
    private final ObjectMapper jsonMapper;
    private final NotificationWebSocketHandler webSocketHandler;

    // Для дедупликации (idempotent consumer)
    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();


    public NotificationListener(ObjectMapper jsonMapper,
                                NotificationWebSocketHandler webSocketHandler) {
        this.jsonMapper = jsonMapper;
        this.webSocketHandler = webSocketHandler;
    }
    @RabbitListener(queues = "q.notifications.all", messageConverter = "")
    public void handleEvent(Message message) {
        System.out.println("ПОЛУЧЕНО СООБЩЕНИЕ!");
        // Парсим метаданные

        JsonNode root = null;
        try {
            root = jsonMapper.readTree(message.getBody());
            EventMetadata metadata = jsonMapper.treeToValue(root.get("metadata"), EventMetadata.class);
            // Дедупликация по eventId
            if (!processedEventIds.add(metadata.getEventId())) return;
            // Формируем уведомление
            String title = buildTitle(metadata.getEventType());
            String description = buildDescription(metadata.getEventType(), root.get("payload"));

            // Сериализуем в JSON для WebSocket
            NotificationMessage notification = new NotificationMessage(title, description, metadata.getEventType());

            String json = jsonMapper.writeValueAsString(notification);

            // Рассылаем всем подключённым браузерам
            webSocketHandler.broadcast(json);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String buildTitle(String eventType){
        switch (eventType){
            case "button.created": return "Новая кнопка";
            case "button.updated": return "Кнопка обновлена";
            case "button.deleted": return "Кнопка удалена";
            case "user.created": return "Новый пользователь";
            case "user.updated": return "Пользователь обновлен";
            case "user.enriched": return "Пользователь обогащён";
            case "user.deleted": return "Пользователь удален";
            default: return "Новое событие";
        }
    }
    private String buildDescription(String eventType,JsonNode payload){
        String firstName = getText(payload, "firstName");
        String lastName = getText(payload, "lastName");
        String visits = getText(payload, "visitsHall");
        int activity = getInt(payload, "estimatedReadingMinutes");
        String level = getText(payload, "difficultyLevel");
        double score = getDouble(payload, "recommendationScore");
        String subscriptionActivation = getSubscriptionActivation(payload, "subscriptionActivation");
        String idUser = getIdUser(payload, "userId");
        String process = getProcess(payload, "process");
        switch (eventType){
            case "user.created":
                return "Создан пользователь \"" + firstName + " " + lastName + "\" Количество посещений: " + visits;
            case "user.updated":
                return "Пользователь обновлен \"" + firstName + " " + lastName
                        + "\" Новая дата активации абонемента: " + subscriptionActivation + ". Количество посещений: \" " + visits;
            case "user.deleted":
                return "Пользователь удален \"" + firstName + " " + lastName;
            case "button.created":
                return "Создан новый абонемент. Для пользователя с id:  \"" + idUser + "\" Процес создания нового абонемента: " + process;
            case "button.updated":
                return "Абонемент обновлен. Для пользователя с id:  \"" + idUser
                        + "\" Процес создания нового абонемента: " + process + ". Количество посещений: \"" + visits;
            case "button.deleted":
                return "Абонемент удален для пользователя с id:  \"" + idUser;
            case "user.enriched":
                return "Пользователь \"" + firstName + " " + lastName +
                        "\" | Активность: " + activity + " мин | Уровень: " + level +
                        " | Балл: " + score;

            default:
                return "Событие: " + eventType;
        }
    }

    private String getSubscriptionActivation(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asText() : "не указано";
    }

    private String getIdUser(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asText() : "не указано";
    }

    private String getProcess(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asText() : "не указано";
    }

    private String getText(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asText() : "не указано";
    }

    private int getInt(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asInt() : 0;
    }

    private double getDouble(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asDouble() : 0.0;
    }
}
