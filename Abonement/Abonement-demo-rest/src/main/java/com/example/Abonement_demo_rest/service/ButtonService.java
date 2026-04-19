package com.example.Abonement_demo_rest.service;

import com.example.AbonementFitness.dto.*;
import com.example.Abonement_demo_rest.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ButtonService {
    private final InMemoryStorage storage;
    private final UserService userService;

    public ButtonService(InMemoryStorage storage, @Lazy UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }


    public ButtonResponse findButtonById(Long id) {
        return Optional.ofNullable(storage.button.get(id))  // ← storage.button.get(id)
                .orElseThrow(() -> new RuntimeException("Button with id: " + id));
    }

    public ButtonResponse patchButton(Long id, PatchButtonRequest request) {
        ButtonResponse existing = findButtonById(id);
        ButtonResponse patchButton = ButtonResponse.builder()
                .userId(existing.getUserId())
                .requestId(id)
                .rejectionReason("Пользователь не нарушал правила")
                .process("3 days")
                .value(request.value() != null ? request.value() : existing.getValue())
                .visitsHall(request.visitsHall())
                .subscriptionActivation(request.subscriptionActivation() != null ?
                        request.subscriptionActivation() : existing.getSubscriptionActivation())
                .endOfSubscription(request.endOfSubscription() != null ?
                        request.endOfSubscription() : existing.getEndOfSubscription())
                .build();
        storage.button.put(id, patchButton);
        userService.newAbonement(id, patchButton);
        return patchButton;
    }

    public ButtonResponse update(Long id, ButtonRequest request) {
        ButtonResponse existing = findButtonById(id);
        ButtonResponse updateButton = ButtonResponse.builder()
                .userId(existing.getUserId())
                .requestId(id)
                .rejectionReason("Пользователь не нарушал правила")
                .process("3 days")
                .value(request.value() != null ? request.value() : existing.getValue())
                .visitsHall(request.visitsHall() != null ? request.visitsHall() : existing.getVisitsHall())
                .subscriptionActivation(request.subscriptionActivation() != null ?
                        request.subscriptionActivation() : existing.getSubscriptionActivation())
                .endOfSubscription(request.endOfSubscription() != null ?
                        request.endOfSubscription() : existing.getEndOfSubscription())
                .build();
        storage.button.put(id, updateButton);
        userService.newAbonement(id, updateButton);
        return updateButton;
    }

    public void delete(Long id) {
        storage.button.remove(id);
    }

    public ButtonResponse proccessRenewal(ButtonRequest request) {
        UserResponse user = userService.findById(request.userId());
        Long renewal = storage.buttonSequence.incrementAndGet();
        ButtonResponse buttonResponse = ButtonResponse.builder()
                .userId(user.getId())
                .requestId(renewal)
                .rejectionReason("Пользователь не нарушал правила")
                .process("3 days")
                .value(request.value())
                .visitsHall(request.visitsHall())
                .subscriptionActivation(request.subscriptionActivation())
                .endOfSubscription(request.endOfSubscription())
                .build();
        storage.button.put(renewal, buttonResponse);
        return buttonResponse;
    }

    public ButtonResponse getAnzByService(Long requestId) {
        return findButtonById(requestId);
    }

    public void deleteButtonByUser(Long id) {
        UserResponse user = userService.findById(id);
        List<ButtonResponse> buttonInStorage = storage.button.values().stream().toList();
        for (ButtonResponse button : buttonInStorage) {
            if (button.getUserId().equals(user.getId())) {
                storage.button.remove(button.getRequestId());
            }
        }
    }

    public PagedResponse<ButtonResponse> findAll(Long userId, int page, int size) {
        Stream<ButtonResponse> stream = storage.button.values().stream()
                .sorted((b1,b2) -> b1.getRequestId().compareTo(b2.getRequestId()));
        if(userId != null){
            stream = stream.filter(b -> b.getUserId() != null && b.getUserId().equals(userId));

        }
        List<ButtonResponse> allButtons = stream.toList();
        int totalElements = allButtons.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<ButtonResponse> content = (from >= totalElements) ? List.of() : allButtons.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);



    }
}