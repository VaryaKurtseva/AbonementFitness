package com.example.AbonementFitness.service.serviceImp;

import com.example.AbonementFitness.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public boolean hasActiveSubscription(Long userId) {
        return false;
    }
}
