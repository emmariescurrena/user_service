package com.emmariescurrena.bookesy.user_service.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {

    @Size(min = 2, max = 100, message = "The length of nickname must be between 2 and 100 characters")
    private String nickname;

    @Size(max = 1000, message = "The maximum length of bio is 1000 characters")
    private String bio;

}
