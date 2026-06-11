package com.example.Abonement_demo_rest.event;

import abonement.demo.events.ButtonEvent;
import abonement.demo.events.EventEnvelope;
import abonement.demo.events.RoutingKeys;
import com.example.AbonementFitness.dto.ButtonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Публикация доменных событий button в RabbitMQ.
 *
 * Аналогичен UserEventPublisher — тот же fire-and-forget паттерн.
 */
@Component
public class ButtonEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(ButtonEventPublisher.class);
    private static final String SOURCE = "demo-rest";

    private final RabbitTemplate rabbitTemplate;

    public ButtonEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Публикует событие «button создан».
     */
    public void publishCreated(ButtonResponse button) {
        var event = new ButtonEvent.Created(
                button.getUserId(),
                button.getUserId(),
                button.getRejectionReason(),
                button.getProcess(),
                button.getValue(),
                button.getVisitsHall(),
                button.getSubscriptionActivation(),
                button.getEndOfSubscription()
        );
        send(RoutingKeys.BUTTON_CREATED, event);
    }
    public void publishUpdated(ButtonResponse button) {
        var event = new ButtonEvent.Updated(
                button.getValue(),
                button.getVisitsHall(),
                button.getSubscriptionActivation(),
                button.getEndOfSubscription()
        );
        send(RoutingKeys.BUTTON_UPDATED, event);
    }

    /**
     * Публикует событие «button удалён» с количеством каскадно удалённых книг.
     */
    public void publishDeleted(ButtonResponse button) {
        var event = new ButtonEvent.Deleted(
                button.getUserId(),
                button.getRequestId()

        );
        send(RoutingKeys.BUTTON_DELETED, event);
    }

    private void send(String routingKey, ButtonEvent event) {
        try {
            EventEnvelope<ButtonEvent> envelope = EventEnvelope.wrap(event, SOURCE, routingKey);
            rabbitTemplate.convertAndSend(RoutingKeys.EXCHANGE, routingKey, envelope);
            log.info("Событие отправлено: {} [eventId={}]", routingKey, envelope.metadata().eventId());
        } catch (Exception e) {
            log.error("Не удалось отправить событие {}: {}", routingKey, e.getMessage());
        }
    }
}
