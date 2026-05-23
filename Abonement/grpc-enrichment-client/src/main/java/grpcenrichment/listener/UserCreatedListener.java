package grpcenrichment.listener;

import abonement.demo.events.EventMetadata;
import abonement.demo.events.UserEvent;
import grpcenrichment.publisher.EnrichmentEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import abonement.demo.grpc.AnalyzeUserRequest;
import abonement.demo.grpc.UserAnalysisResponse;
import abonement.demo.grpc.UserAnalyticsGrpc;
/**
 * Слушатель событий user.created из RabbitMQ.
 *
 * Десериализация — ручная (как в audit-service), потому что EventEnvelope<T>
 * является generic-типом, и Jackson не может определить конкретный подтип T.
 */
@Component
public class UserCreatedListener {
    private static final Logger log = LoggerFactory.getLogger(UserCreatedListener.class);

    private final UserAnalyticsGrpc.UserAnalyticsBlockingStub analyticsStub;
    private final EnrichmentEventPublisher enrichmentPublisher;
    private final JsonMapper jsonMapper;

    public UserCreatedListener(UserAnalyticsGrpc.UserAnalyticsBlockingStub analyticsStub,
                               EnrichmentEventPublisher enrichmentPublisher,
                               JsonMapper jsonMapper) {
        this.analyticsStub = analyticsStub;
        this.enrichmentPublisher = enrichmentPublisher;
        this.jsonMapper = jsonMapper;
    }
    /**
     * Обрабатывает событие user.created:
     * 1. Десериализует событие из JSON
     * 2. Формирует gRPC-запрос
     * 3. Вызывает gRPC-сервер (синхронно)
     * 4. Публикует результат как событие user.enriched
     */


    @RabbitListener(queues = "q.enrichment.user-created", messageConverter = "")
    public void handleUserCreated(Message message) {
        try {
            // 1. Парсим JSON-конверт
            byte[] body = message.getBody();
            JsonNode root = jsonMapper.readTree(body);

            JsonNode metaNode = root.get("metadata");
            EventMetadata metadata = jsonMapper.treeToValue(metaNode, EventMetadata.class);

            JsonNode payloadNode = root.get("payload");
            UserEvent.Created userCreated = jsonMapper.treeToValue(payloadNode, UserEvent.Created.class);

            log.info("Получено событие user.created: id={}, «{}» [eventId={}]",
                    userCreated.id(), userCreated.firstName(), metadata.eventId());

            // 2. Формируем gRPC-запрос
            AnalyzeUserRequest grpcRequest = AnalyzeUserRequest.newBuilder()
                    .setUserId(userCreated.id())
                    .setFirstName(userCreated.firstName())
                    .setLastName(userCreated.lastName())
                    .setEmail(userCreated.email())
                    .setSubscriptionActivation(userCreated.subscriptionActivation().toString())
                    .setEndOfSubscription(userCreated.endOfSubscription().toString())
                    .setVisitsHall(userCreated.visitsHall())
                    .setNumberPhone(userCreated.numberPhone())
                    .build();


            // 3. Вызываем gRPC-сервер (синхронно)
            log.info("Вызов gRPC: UserAnalytics.AnalyzeUser(userId={})", userCreated.id());
            UserAnalysisResponse grpcResponse = analyticsStub.analyzeUser(grpcRequest);

            log.info("gRPC ответ получен: id={}, активность={}мин, уровень={}, балл={}, категория={}",
                    grpcResponse.getUserId(),
                    grpcResponse.getEstimatedActivityMinutes(),
                    grpcResponse.getFitnessLevel(),
                    grpcResponse.getRecommendationScore(),
                    grpcResponse.getSubscriptionTier());

            // 4. Публикуем событие user.enriched
            UserEvent.Enriched enrichedEvent = new UserEvent.Enriched(
                    grpcResponse.getUserId(),
                    userCreated.firstName(),
                    userCreated.lastName(),
                    grpcResponse.getEstimatedActivityMinutes(),
                    grpcResponse.getFitnessLevel(),
                    grpcResponse.getRecommendationScore(),
                    grpcResponse.getSubscriptionTier()

            );

            enrichmentPublisher.publishEnriched(enrichedEvent);

            log.info("User обогащена: id={}, «{}» и user.enriched отправлено",
                    userCreated.id(), userCreated.firstName());

        } catch (io.grpc.StatusRuntimeException e) {
            log.error("gRPC ошибка при обогащении книги: {} ({})",
                    e.getStatus().getDescription(), e.getStatus().getCode());
            throw new RuntimeException("gRPC-вызов завершился ошибкой", e);

        } catch (Exception e) {
            log.error("Ошибка обработки события book.created: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обработать событие book.created", e);
        }
    }
}
