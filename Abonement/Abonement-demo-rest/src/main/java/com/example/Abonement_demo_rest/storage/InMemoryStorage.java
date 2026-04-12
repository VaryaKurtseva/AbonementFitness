package com.example.Abonement_demo_rest.storage;

import com.example.AbonementFitness.dto.ButtonRenewResponse;
import com.example.AbonementFitness.dto.UserResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {
    public final Map<Long, UserResponse> users = new ConcurrentHashMap<>();
    public final Map<Long, ButtonRenewResponse> button = new ConcurrentHashMap<>();

    public final AtomicLong userSequence = new AtomicLong(0);
    public final AtomicLong buttonSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        // Создаем пользователей с полным набором полей (демонстрация Lombok @Builder)
        UserResponse user1 = UserResponse.builder()
                .id(userSequence.incrementAndGet())
                .firstName("Варвара")
                .lastName("Курцева")
                .fullName("Курцева Варвара")
                .email("vara6021@gmail.com")
                .subscriptionActivation(LocalDate.of(2026,12,7))
                .endOfSubscription(LocalDate.of(2027,12,7))
                .visitsHall(40)
                .build();

        UserResponse user2 = UserResponse.builder()
                .id(userSequence.incrementAndGet())
                .firstName("Виктория")
                .lastName("Ким")
                .fullName("Ким Виктория")
                .email("kikavim@gmail.com")
                .subscriptionActivation(LocalDate.of(2026,12,7))
                .endOfSubscription(LocalDate.of(2027,12,7))
                .visitsHall(12)
                .build();

        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);

        // Создаем ответ от системы на продление абонементов с полным набором полей
        long button1 = buttonSequence.incrementAndGet();
        button.put(button1, ButtonRenewResponse.builder()
                        .userId(user1.getId())
                .requestId(user1.getId())
                .rejectionReason("Пользователь нарушил правило клуба - Не подсказывать другим во время тренировки")
                .process("3 дня на обработку запроса")
                .build());


        long button2 = buttonSequence.incrementAndGet();
        button.put(button2, ButtonRenewResponse.builder()
                        .userId(user2.getId())
                .requestId(user2.getId())
                .rejectionReason("Пользователь не нарушил ни одного правило клуба")
                .process("1 день на обработку запроса")
                .build());


    }
}
