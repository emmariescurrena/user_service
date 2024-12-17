package com.emmariescurrena.bookesy.user_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.emmariescurrena.bookesy.user_service.enums.BookStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
@Table("my_bookshelf_books")
public class MyBookshelfBook {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column("user_id")
    private Long userId;

    @Column("book_id")
    private String bookId;

    @Column("book_status")
    @Enumerated(EnumType.STRING)
    private BookStatusEnum bookStatus;

}
