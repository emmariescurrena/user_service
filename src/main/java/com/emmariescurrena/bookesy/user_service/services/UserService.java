package com.emmariescurrena.bookesy.user_service.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emmariescurrena.bookesy.user_service.dtos.CreateUserDto;
import com.emmariescurrena.bookesy.user_service.dtos.UpdateUserDto;
import com.emmariescurrena.bookesy.user_service.models.RoleEnum;
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.repositories.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;

    public Mono<User> createUser(CreateUserDto userDto) {
        User newUser = new User();
        BeanUtils.copyProperties(userDto, newUser);

        return userRepository.save(newUser);
    }

    public Mono<User> createSuperAdmin(CreateUserDto userDto) {
        User newUser = new User();
        BeanUtils.copyProperties(userDto, newUser);
        newUser.setRole(RoleEnum.SUPER_ADMIN);

        return userRepository.save(newUser);
    }

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Mono<User> getUserByAuth0UserId(String auth0UserId) {
        return userRepository.findByAuth0UserId(auth0UserId);
    }

    public Mono<User> updateUser(User userToUpdate, UpdateUserDto userDto) {
        BeanUtils.copyProperties(userDto, userToUpdate);
        return userRepository.save(userToUpdate);
    }

    public Mono<Void> deleteUser(User user) {
        userRepository.delete(user);
        return Mono.empty();
    }

}
