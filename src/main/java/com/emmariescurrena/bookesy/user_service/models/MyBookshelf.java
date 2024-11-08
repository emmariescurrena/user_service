package com.emmariescurrena.bookesy.user_service.models;

import com.emmariescurrena.bookesy.user_service.enums.BookStatusEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "MY_BOOKSHELF", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "book_id"})})
public class MyBookshelf {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatusEnum status;

}
