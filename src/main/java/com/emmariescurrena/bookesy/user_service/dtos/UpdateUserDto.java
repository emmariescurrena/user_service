package com.emmariescurrena.bookesy.user_service.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {

    @Size(min = 2, max = 100, message = "The length of name must be between 2 and 100 characters")
    private String name;

    @Size(min = 2, max = 100, message = "The length of surname must be between 2 and 100 characters")
    private String surname;

    @Size(max = 1000, message = "The maximum length of bio is 1000 characters")
    private String bio;

}
