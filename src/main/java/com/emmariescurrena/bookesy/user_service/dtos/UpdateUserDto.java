package com.emmariescurrena.bookesy.user_service.dtos;

import com.emmariescurrena.bookesy.user_service.util.RegexValidator;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {

    private String auth0UserId;

    @Email(regexp = RegexValidator.EMAIL,
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid email format")
    private String email;

    @NotEmpty(message = "The username is required")
    @Size(min = 2, max = 15, message = "The length of name must be between 2 and 15 characters")
    private String username;

    @Size(min = 2, max = 100, message = "The length of name must be between 2 and 100 characters")
    private String name;

    @Size(min = 2, max = 100, message = "The length of surname must be between 2 and 100 characters")
    private String surname;

    @Size(max = 1000, message = "The maximum length of bio is 1000 characters")
    private String bio;

}
