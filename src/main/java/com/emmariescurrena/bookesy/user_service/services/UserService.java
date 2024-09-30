package com.emmariescurrena.bookesy.user_service.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emmariescurrena.bookesy.user_service.models.RoleEnum;
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        user.setRole(RoleEnum.USER);

        return userRepository.save(user);

    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long userId, User updatedUser) {
        User userToUpdate = userRepository.getReferenceById(userId);

        Optional<String> updatedEmail = Optional.of(updatedUser.getEmail());
        if (updatedEmail.isPresent()) {
            userToUpdate.setEmail(updatedEmail.get());
        }

        Optional<String> updatedBio = Optional.of(updatedUser.getBio());
        if (updatedBio.isPresent()) {
            userToUpdate.setBio(updatedBio.get());
        }

        return userRepository.save(userToUpdate);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

}
