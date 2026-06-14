package webSocket.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import webSocket.handler.NotificationWebSocketHandler;

@Configuration
@EnableWebSocket  // включает поддержку WebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final NotificationWebSocketHandler notificationHandler;

    public WebSocketConfig(NotificationWebSocketHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(notificationHandler, "/ws/notifications")
                .setAllowedOrigins("*");  // CORS – для разработки
    }
}