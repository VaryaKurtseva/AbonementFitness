package com.example.Abonement_demo_rest.service;

import com.example.AbonementFitness.dto.*;
import com.example.Abonement_demo_rest.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ButtonService {
    private final InMemoryStorage storage;
    private final UserService userService;

    public ButtonService(InMemoryStorage storage,@Lazy UserService userService) {
        this.storage = storage;
        this.userService = userService;
    }
    private ButtonRenewResponse findButtonById(Long id) {
        return Optional.ofNullable(storage.button.get(id))
                .orElseThrow(() -> new RuntimeException("Button with id: " + String.valueOf(id)));

    }

    public ButtonRenewResponse patchButton(Long id, PatchButtonRequest request) {
        ButtonRenewResponse existing = findButtonById(id);
        ButtonRenewResponse patchButton = ButtonRenewResponse.builder()
                .userId(existing.getUserId())
                .requestId(id)
                .rejectionReason("Пользователь не нарушал правила")
                .process("3 days")
                .value(2)
                .visitsHall(20)
                .subscriptionActivation(request.subscriptionActivation())
                .endOfSubscription(request.endOfSubscription())
                .build();
        storage.button.put(id, patchButton);
        userService.newAbonement(id, patchButton);
        return patchButton;
    }



    public ButtonRenewResponse update(Long id, ButtonRenewSubscriptionRequest request) {
        ButtonRenewResponse existing = findButtonById(id);
        ButtonRenewResponse updateButton = ButtonRenewResponse.builder()
                .userId(existing.getUserId())
                .requestId(id)
                .rejectionReason("Пользователь не нарушал правила")
                .process("3 days")
                .value(request.value() != null ? request.value() : existing.getValue())
                .visitsHall(request.visitsHall() != null ? request.visitsHall() : existing.getVisitsHall())
                .subscriptionActivation(request.subscriptionActivation() != null ? request.subscriptionActivation() : existing.getSubscriptionActivation())
                .endOfSubscription(request.endOfSubscription() != null ? request.endOfSubscription() : existing.getEndOfSubscription())
                .build();
        storage.button.put(id, updateButton);
        userService.newAbonement(id, updateButton);
        return updateButton;
    }

    public void delete(Long id) {
        ButtonRenewResponse button = findButtonById(id);
        storage.button.remove(id);


    }

    public ButtonRenewResponse proccessRenewal(ButtonRenewSubscriptionRequest request) {
        UserResponse user = userService.findById(request.userId());
        Long renewal = storage.buttonSequence.incrementAndGet();
        ButtonRenewResponse buttonRenewResponse = ButtonRenewResponse.builder()
                .userId(user.getId())
                .requestId(renewal)
                .rejectionReason("Пользователь не нарушал правила")
                .process("3 days")
                .value(request.value())
                .visitsHall(request.visitsHall())
                .subscriptionActivation(request.subscriptionActivation())
                .endOfSubscription(request.endOfSubscription())
                .build();
        storage.button.put(renewal, buttonRenewResponse);
        return buttonRenewResponse;
    }

    public ButtonRenewResponse getAnzByService(Long requestId) {
        return findButtonById(requestId);
    }

    public void deleteButtonByUser(Long id) {
        UserResponse user = userService.findById(id);
        List<ButtonRenewResponse> buttonInStorage = storage.button.values().stream().toList();
        for(int i = 0; i < buttonInStorage.size(); i++){
            if(buttonInStorage.get(i).getUserId() == user.getId()){
                storage.button.remove(i);
            }
        }

    }
}
