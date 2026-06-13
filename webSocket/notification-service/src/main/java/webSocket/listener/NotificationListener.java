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
            String description = buildDescription(metadata.getEventType(), root.get("payload"));                  // "Создана книга «Война и мир»"

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
            case "book.created": return "Новая книга";
            case "book.enriched": return "Книга обогащена";
            case "author.created": return "Новый автор";
            case "author.deleted": return "Автор удален";
            default: return "Новое событие";
        }
    }
    private String buildDescription(String eventType,JsonNode payload){
        switch (eventType){
            case "book.created":
                String title = payload.get("title").asText();
                String author = payload.get("authorFullName").asText();
                return "Создана книга \"" + title + "\" (" + author + ")";
            case "book.enriched":
                String bookTitle = payload.get("title").asText();
                int minutes = payload.get("estimatedReadingMinutes").asInt();
                return "Книга \"" + bookTitle + "\" | Время чтения: " + minutes + " мин";
            case "author.created":
                String name = payload.get("fullName").asText();
                return "Создан автор \"" + name + "\"";
            case "author.deleted":
                String delName = payload.get("fullName").asText();
                return "Удалён автор \"" + delName + "\"";
            default:
                return "Событие: " + eventType;
        }
    }
}
