package grpcenrichment.publisher;

import abonement.demo.events.EventEnvelope;
import abonement.demo.events.RoutingKeys;
import abonement.demo.events.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Публикация событий обогащения (user.enriched) в RabbitMQ.
 *
 * Аналогичен UserEventPublisher в demo-rest, но публикует другой тип события.
 * Паттерн fire-and-forget: если RabbitMQ недоступен, ошибка логируется,
 * но gRPC-вызов уже выполнен — результат не теряется полностью.
 */
@Component
public class EnrichmentEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(EnrichmentEventPublisher.class);
    private static final String SOURCE = "grpc-enrichment-client";

    private final RabbitTemplate rabbitTemplate;

    public EnrichmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    /**
     * Публикует событие user.enriched с результатами gRPC-аналитики.
     */
    public void publishEnriched(UserEvent.Enriched enrichedEvent){
        try {
            EventEnvelope<UserEvent> envelope = EventEnvelope.wrap(
                    enrichedEvent, SOURCE, RoutingKeys.USER_ENRICHED);
            rabbitTemplate.convertAndSend(
                    RoutingKeys.EXCHANGE,
                    RoutingKeys.USER_ENRICHED,
                    envelope
            );
            log.info("Событие отправлено: {} [userId={}, eventId={}]",
                    RoutingKeys.USER_ENRICHED,
                    enrichedEvent.id(),
                    envelope.metadata().eventId());

        }catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}",
                    RoutingKeys.USER_ENRICHED, e.getMessage());
        }
    }
}
