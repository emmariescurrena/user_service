package com.emmariescurrena.bookesy.user_service.dtos;

import com.emmariescurrena.bookesy.user_service.util.RegexValidator;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserDto {
    
    @NotEmpty(message = "The email is required")
    @Email(regexp = RegexValidator.EMAIL,
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid email format")
    private String email;   

}
