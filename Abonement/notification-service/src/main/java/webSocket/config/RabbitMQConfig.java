package webSocket.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "button.events";
    public static final String NOTIFICATIONS_QUEUE = "q.notifications.all";
    public static final String ROUTING_KEY = "#"; // все события
    @PostConstruct
    public void init() {
        System.out.println("RabbitMQConfig ЗАГРУЖЕН!");
    }

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue notificationsQueue() {
         return QueueBuilder.durable(NOTIFICATIONS_QUEUE).build();
    }

    @Bean
    public Binding notificationsBinding(Queue notificationsQueue, TopicExchange eventsExchange) {
        return BindingBuilder
                .bind(notificationsQueue)
                .to(eventsExchange)
                .with(ROUTING_KEY);
    }
}