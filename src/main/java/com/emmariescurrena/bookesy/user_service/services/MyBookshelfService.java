package com.emmariescurrena.bookesy.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emmariescurrena.bookesy.user_service.dtos.UpsertMyBookshelfBookDto;
import com.emmariescurrena.bookesy.user_service.exceptions.NotFoundException;
import com.emmariescurrena.bookesy.user_service.models.MyBookshelfBook;
import com.emmariescurrena.bookesy.user_service.repositories.MyBookshelfBookRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MyBookshelfService {
    
    @Autowired
    MyBookshelfBookRepository myBookshelfBookRepository;

    @Autowired
    UserService userService;

    public Mono<Void> upsertBook(Long userId, UpsertMyBookshelfBookDto bookDto) {
        return myBookshelfBookRepository.findByUserIdAndBookId(userId, bookDto.getBookId())
            .switchIfEmpty(Mono.defer(() -> {
                MyBookshelfBook myBookshelfBook = new MyBookshelfBook();
                myBookshelfBook.setUserId(userId);
                myBookshelfBook.setBookId(bookDto.getBookId());
                return Mono.just(myBookshelfBook);
            }))
            .flatMap(myBookshelfBook -> {
                myBookshelfBook.setBookStatus(bookDto.getBookStatus());
                return myBookshelfBookRepository.save(myBookshelfBook).then();
            });
    }

    public Flux<String> getUserBookshelfBooks(Long userId) {
        return myBookshelfBookRepository.findByUserId(userId).map(MyBookshelfBook::getBookId);
    }

    public Mono<Void> removeBook(Long userId, String bookId) {
        return myBookshelfBookRepository.findByUserIdAndBookId(userId, bookId)
            .switchIfEmpty(Mono.error(new NotFoundException("Book not found in the shelf")))
            .flatMap(book -> {
                return myBookshelfBookRepository.delete(book).then();
            });
    }

}
