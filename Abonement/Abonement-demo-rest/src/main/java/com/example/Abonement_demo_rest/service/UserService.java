package com.example.Abonement_demo_rest.service;

import com.example.AbonementFitness.dto.*;
import com.example.Abonement_demo_rest.storage.InMemoryStorage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final InMemoryStorage storage;
    private final ButtonService buttonService;

    public UserService(InMemoryStorage storage, @Lazy ButtonService buttonService) {
        this.storage = storage;
        this.buttonService = buttonService;
    }
    public PagedResponse<UserResponse> findAll(int page, int size) {
        List<UserResponse> all = storage.users.values().stream()
                .sorted(Comparator.comparingLong(UserResponse::getId))
                .toList();
        int totalElements = all.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<UserResponse> content = (from >= totalElements) ? List.of() : all.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public UserResponse findById(Long id) {
        return Optional.ofNullable(storage.users.get(id))
                .orElseThrow(() -> new RuntimeException("Пользователь с id: " + String.valueOf(id) + " не найден"));
    }

    public PagedResponse<UserResponse> searchByName(String query, int page, int size) {
        List<UserResponse> all = storage.users.values().stream()
                .sorted(Comparator.comparingLong(UserResponse::getId))
                .toList();
        List<UserResponse> byName = new ArrayList<>();
        for(int i = 0; i < all.size(); i++){
            if(all.get(i).getFirstName().equalsIgnoreCase(query)) byName.add(all.get(i));
        }
        int totalElements = byName.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<UserResponse> content = (from >= totalElements) ? List.of() : byName.subList(from, to);
        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public UserResponse create(UserRequest request) {
        long id = storage.userSequence.incrementAndGet();
        String fullName = request.firstName() + " " + request.lastName();
        UserResponse user = UserResponse.builder()
                .id(id)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .fullName(fullName)
                .email(request.email())
                .numberPhone(request.numberPhone())
                .subscriptionActivation(request.subscriptionActivation())
                .endOfSubscription(request.endOfSubscription())
                .visitsHall(request.visitsHall())
                .build();
        storage.users.put(id,user);
        return user;

    }

    public UserResponse update(Long id, UpdateUserRequest request) {
        UserResponse existing = findById(id);
        String fullName = request.firstName() + " " + request.lastName();
        UserResponse updateUser = UserResponse.builder()
                .id(id)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .fullName(fullName)
                .email(request.email())
                .numberPhone(request.numberPhone())
                .subscriptionActivation(request.subscriptionActivation())
                .endOfSubscription(request.endOfSubscription())
                .visitsHall(request.visitsHall())
                .build();
        storage.users.put(id,updateUser);
        return updateUser;
    }

    public UserResponse patchUser(Long id, PatchUserRequest request) {
        UserResponse existing = findById(id);
        String fullName = request.firstName() + " " + request.lastName();
        UserResponse updateUser = UserResponse.builder()
                .id(id)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .fullName(fullName)
                .email(request.email() != null ? request.email() : existing.getEmail())
                .numberPhone(request.numberPhone() != null ? request.numberPhone() : existing.getNumberPhone())
                .subscriptionActivation(request.subscriptionActivation() != null ? request.subscriptionActivation() : existing.getSubscriptionActivation())
                .endOfSubscription(request.endOfSubscription() != null ? request.endOfSubscription() : existing.getEndOfSubscription())
                .visitsHall(request.visitsHall() != null ? request.visitsHall() : existing.getVisitsHall())
                .build();
        storage.users.put(id,updateUser);
        return updateUser;
    }

    public void delete(Long id) {
        UserResponse existing = findById(id);
        buttonService.deleteButtonByUser(id);
        storage.users.remove(id);
        
    }

    public void newAbonement(Long id, ButtonRenewResponse patchButton) {
    }


}
