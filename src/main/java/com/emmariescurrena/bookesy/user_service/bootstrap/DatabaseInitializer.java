package com.emmariescurrena.bookesy.user_service.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.emmariescurrena.bookesy.user_service.dtos.CreateUserDto;
import com.emmariescurrena.bookesy.user_service.services.UserService;

import reactor.core.publisher.Mono;

@Component
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;

    @Value("${super-admin.nickname}")
    private String superAdminNickname;

    @Value("${super-admin.email}")
    private String superAdminEmail;

    @Value("${super-admin.auth0-user-id}")
    private String superAdminAuth0UserId;

    @Value("${regular-user.nickname}")
    private String regularUserNickname;

    @Value("${regular-user.email}")
    private String regularUserEmail;

    @Value("${regular-user.auth0-user-id}")
    private String regularUserAuth0UserId;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        createSuperAdministrator();
        createRegularUser();
    }

    private void createSuperAdministrator() {
        CreateUserDto userDto = new CreateUserDto();

        userDto.setEmail(superAdminEmail);
        userDto.setNickname(superAdminNickname);
        userDto.setAuth0UserId(superAdminAuth0UserId);

        userService.getUserByEmail(userDto.getEmail())
        .flatMap(existingUser -> {
            return Mono.empty();
        })
        .switchIfEmpty(
            userService.createSuperAdmin(userDto)
        );
    }

    private void createRegularUser() {
        CreateUserDto userDto = new CreateUserDto();

        userDto.setEmail(regularUserEmail);
        userDto.setNickname(regularUserNickname);
        userDto.setAuth0UserId(regularUserAuth0UserId);

        userService.getUserByEmail(regularUserEmail)
        .flatMap(existingUser -> {
            return Mono.empty();
        })
        .switchIfEmpty(
            userService.createUser(userDto)
        )
        .subscribe();
    }

}
