package com.emmariescurrena.bookesy.user_service.bootstrap;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.emmariescurrena.bookesy.user_service.dtos.CreateUserDto;
import com.emmariescurrena.bookesy.user_service.models.User;
import com.emmariescurrena.bookesy.user_service.services.UserService;

@Component
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    UserService userService;

    @Value("${super-admin.name}")
    private String superAdminName;

    @Value("${super-admin.surname}")
    private String superAdminSurname;

    @Value("${super-admin.email}")
    private String superAdminEmail;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {

        CreateUserDto userDto = new CreateUserDto();

        userDto.setEmail(superAdminEmail);
        userDto.setName(superAdminName);
        userDto.setSurname(superAdminSurname);

        Optional<User> optionalUser = userService.getUserByEmail(userDto.getEmail());

        if (optionalUser.isPresent()) {
            return;
        }

        userService.createUser(userDto);

    }

}
