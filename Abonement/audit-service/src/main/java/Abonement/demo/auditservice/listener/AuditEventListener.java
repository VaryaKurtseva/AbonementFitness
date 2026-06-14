package Abonement.demo.auditservice.listener;




import Abonement.demo.auditservice.model.AuditEntry;
import Abonement.demo.auditservice.storage.AuditStorage;

import abonement.demo.events.*;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

/**
 * Единый слушатель всех доменных событий из RabbitMQ.
 *
 * Принимает «сырое» AMQP-сообщение (Message) и десериализует его вручную.
 * Это необходимо, потому что EventEnvelope<T> — generic тип, и Jackson
 * не может определить конкретный подтип T при автоматической десериализации.
 *
 */
@Component
public class AuditEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuditEventListener.class);

    private final AuditStorage auditStorage;
    private final JsonMapper jsonMapper;

    public AuditEventListener(AuditStorage auditStorage, JsonMapper jsonMapper) {
        this.auditStorage = auditStorage;
        this.jsonMapper = jsonMapper;
    }

    /**
     * Принимает все события из очереди q.audit.events.
     *
     * Десериализация выполняется в два этапа:
     * 1. Парсим JSON в дерево узлов (JsonNode) — быстро и безопасно.
     * 2. Извлекаем metadata и определяем тип payload по полю eventType.
     * 3. Десериализуем payload в конкретный record по выявленному типу.
     */
    @RabbitListener(queues = "q.audit.events", messageConverter = "")
    public void handleEvent(Message message) {
        try {
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            // Извлекаем метаданные из JSON-конверта
            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            // Дедупликация — если событие уже обработано, пропускаем
            if (auditStorage.isDuplicate(metadata.eventId())) {
                log.warn("Дубликат события пропущен: eventId={}", metadata.eventId());
                return;
            }

            // Определяем тип события и формируем описание
            JsonNode payloadNode = root.get("payload");
            String description = buildDescription(metadata.eventType(), payloadNode);

            AuditEntry entry = auditStorage.save(new AuditEntry(
                    0,
                    metadata.eventId(),
                    metadata.eventType(),
                    metadata.source(),
                    metadata.timestamp(),
                    Instant.now(),
                    description
            ));

            log.info("[AUDIT #{}] {} | {}", entry.sequenceNumber(), metadata.eventType(), description);

        } catch (Exception e) {
            log.error("Ошибка обработки события: {}", e.getMessage(), e);
            // Исключение пробросится, сообщение уйдёт в DLQ после исчерпания retries
            throw new RuntimeException("Не удалось обработать событие", e);
        }
    }

    /**
     * Формирует человекочитаемое описание события для аудит-лога.
     *
     * Десериализует payload в конкретный тип на основе eventType,
     * затем формирует описание через pattern matching по sealed interface.
     */
    private String buildDescription(String eventType, JsonNode payloadNode) throws Exception {
        return switch (eventType) {
            case "button.created" -> {
                ButtonEvent.Created e = jsonMapper.treeToValue(payloadNode, ButtonEvent.Created.class);
                yield String.format("Создана button userId=%d requestId=%d количество посещений=%d ",
                        e.userId(), e.requestId(), e.visitsHall());
            }
            case "button.updated" -> {
                ButtonEvent.Updated e = jsonMapper.treeToValue(payloadNode, ButtonEvent.Updated.class);
                yield String.format("Обновлена button количество посещений=%d ", e.value());
            }
            case "button.deleted" -> {
                ButtonEvent.Deleted e = jsonMapper.treeToValue(payloadNode, ButtonEvent.Deleted.class);
                yield String.format("Удалена button requestId=%d userId", e.requestId(), e.userId());
            }
            case "user.created" -> {
                UserEvent.Created e = jsonMapper.treeToValue(payloadNode, UserEvent.Created.class);
                yield String.format("Создан user «%s» «%s» (email: %s)",
                        e.firstName(), e.lastName(), e.email());
            }
            case "user.updated" -> {
                UserEvent.Updated e = jsonMapper.treeToValue(payloadNode, UserEvent.Updated.class);
                yield String.format("Update user «%s» «%s» (email: %d)",
                        e.firstName(), e.lastName(), e.visitsHall());
            }
            case "user.deleted" -> {
                UserEvent.Deleted e = jsonMapper.treeToValue(payloadNode, UserEvent.Deleted.class);
                yield String.format("Удалён user id=%d «%s» «%s» (удалено button: %d)",
                        e.id(), e.firstName(), e.lastName(), e.deletedButtonCounts());
            }
            case "user.enriched" -> {
                UserEvent.Enriched e = jsonMapper.treeToValue(payloadNode,UserEvent.Enriched.class);
                yield String.format("Пользователь обогащена id=%d «%s» «%s»  (время провождения в зале: %dмин, сложность: %s, балл: %.1f, эпоха: %s)",
                        e.id(), e.firstName(), e.lastName() ,e.estimatedReadingMinutes(),
                        e.difficultyLevel(), e.recommendationScore(), e.eraClassification());
            }
            case "button.enriched" -> {
                ButtonEvent.Enriched e = jsonMapper.treeToValue(payloadNode,ButtonEvent.Enriched.class);
                yield String.format("Кнопка обогащена userId=%d requestId=%d (время провождения в зале: %dмин, сложность: %s, балл: %.1f, эпоха: %s)",
                        e.userId(), e.requestId(),  e.estimatedReadingMinutes(),
                        e.difficultyLevel(), e.recommendationScore(), e.eraClassification());
            }
            default -> "Неизвестное событие: " + eventType;
        };
    }
}
