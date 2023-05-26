package com.edisonkastrati.bookapp.controller;

import com.edisonkastrati.bookapp.entity.Book;
import com.edisonkastrati.bookapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://loaclhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization")String token){
        String userEmail = "testuser@email.com";
        return bookService.currentLoansCount(userEmail);
    }

    @GetMapping("/secure/ischeckout/byuser")
    public Boolean checkoutBookByUser(@RequestHeader(value = "Authorization")String token, @RequestParam Long bookId){
        String userEmail = "testuser@email.com";
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    @PutMapping("secure/checkout")
    public Book checkoutBook(@RequestHeader(value = "Authorization")String token, @RequestParam Long bookId) throws Exception{
        String userEmail = "testuser@email.com";
        return bookService.checkoutBook(userEmail, bookId);
    }

}
