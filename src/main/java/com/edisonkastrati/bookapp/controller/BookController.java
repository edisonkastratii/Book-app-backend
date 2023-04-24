package com.edisonkastrati.bookapp.controller;

import com.edisonkastrati.bookapp.entity.Book;
import com.edisonkastrati.bookapp.service.BookServiceImp;
import com.edisonkastrati.bookapp.entity.Book;
import com.edisonkastrati.bookapp.service.BookServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api")
public class BookController{

    private BookServiceImp bookService;

    @Autowired
    public BookController(BookServiceImp bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/book")
    public List<Book> getBooks(){
        return bookService.findAll();
    }

    @GetMapping("/book/{bookId}")
    public Optional<Book> getBook(@PathVariable int bookId){
        Optional<Book> book = bookService.findById(bookId);

        if (book == null) {
            throw new RuntimeException("Book not found! " + bookId);
        }
        return book;
    }

    @PostMapping("/book")
    public Book addBook(@RequestBody Book book){
        book.setId(0);
        Book dbBook = bookService.save(book);
        return dbBook;
    }

    @PutMapping("/book")
    public Book updateBook(@RequestBody Book book){
        Book dbBook =bookService.save(book);
        return dbBook;
    }

    @DeleteMapping("/book/{bookId}")
    public String deleteBook(@PathVariable int bookId){
        bookService.deleteById(bookId);
        return "Delete successfully";
    }
}
