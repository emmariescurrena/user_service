package com.emmariescurrena.bookesy.user_service.dtos;

import lombok.Data;

@Data
public class CreateUserDto {

    private String auth0UserId;
    private String email;
    private String name;
    private String surname;
    private String bio;

}
