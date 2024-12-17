package com.emmariescurrena.bookesy.user_service.dtos;

import com.emmariescurrena.bookesy.user_service.enums.BookStatusEnum;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpsertMyBookshelfBookDto {
    
    @NotEmpty(message = "The book id is required")
    private String bookId;

    @NotEmpty(message = "The book status is required")
    private BookStatusEnum bookStatus;

}
