package com.example.Abonement_demo_rest.event;

import abonement.demo.events.EventEnvelope;
import abonement.demo.events.RoutingKeys;
import abonement.demo.events.UserEvent;
import com.example.AbonementFitness.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Публикация доменных событий пользователя в RabbitMQ.
 *
 * Паттерн: UserService вызывает publish-метод ПОСЛЕ успешного завершения
 * бизнес-операции. Если RabbitMQ недоступен — событие логируется как ошибка,
 * но основная операция (создание/удаление книги) НЕ откатывается.
 *
 * Это паттерн «fire-and-forget» — допустимая потеря события лучше,
 * чем отказ бизнес-операции из-за недоступности брокера.
 *
 * В промышленных системах для гарантированной доставки используют:
 * - Transactional Outbox (запись события в БД в одной транзакции с данными),
 * - Change Data Capture (Debezium/Kafka Connect).
 */
@Component
public class UserEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(UserEventPublisher.class);
    private static final String SOURCE = "demo-rest";

    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Публикует событие «пользователь создан».
     */
    public void publishCreated(UserResponse user) {
        var event = new UserEvent.Created(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getSubscriptionActivation(),
                user.getEndOfSubscription(),
                user.getVisitsHall(),
                user.getNumberPhone()
        );
        send(RoutingKeys.USER_CREATED, event);
    }

    /**
     * Публикует событие «user обновлен».
     */
    public void publishUpdated(UserResponse user) {
        var event = new UserEvent.Updated(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getSubscriptionActivation(),
                user.getEndOfSubscription(),
                user.getVisitsHall(),
                user.getNumberPhone()
        );
        send(RoutingKeys.USER_UPDATED, event);
    }

    /**
     * Публикует событие «user удален».
     */
    public void publishDeleted(Long id,
                               String firstName,
                               String lastName,
                               int deletedButtonCounts) {
        var event = new UserEvent.Deleted(
                id,firstName,lastName,deletedButtonCounts)
                ;
        send(RoutingKeys.USER_DELETED, event);
    }

    /**
     * Отправляет событие в RabbitMQ, обёрнутое в EventEnvelope.
     *
     * convertAndSend:
     * - 1й аргумент: имя exchange
     * - 2й аргумент: routing key (определяет, в какие очереди попадёт сообщение)
     * - 3й аргумент: объект, который Jackson сериализует в JSON
     *
     * try-catch гарантирует, что ошибка публикации не сломает основной бизнес-поток.
     */
    private void send(String routingKey, UserEvent event) {
        try {
            EventEnvelope<UserEvent> envelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, envelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}
