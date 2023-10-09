package dev.kiri.bankAPI.application.service.user;

import dev.kiri.bankAPI.domain.User;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId);
}
