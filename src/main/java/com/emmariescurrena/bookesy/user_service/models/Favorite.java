package com.emmariescurrena.bookesy.user_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("favorites")
public class Favorite {

    @Id
    private Long id;

    @JsonIgnore
    @Column("user_id")
    private Long userId;

    @Column("book_id")
    private String bookId;

    public Favorite(Long userId, String bookId) {
        setUserId(userId);
        setBookId(bookId);
    }

}
