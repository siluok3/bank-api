package dev.kiri.bankAPI.application.service.user;

import dev.kiri.bankAPI.adapter.persistence.UserRepository;
import dev.kiri.bankAPI.application.exception.DuplicateException;
import dev.kiri.bankAPI.application.exception.NotFoundException;
import dev.kiri.bankAPI.domain.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateException("User already exists for username: "+user.getUsername());
        }

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User was not found for id: "+userId));
    }
}
