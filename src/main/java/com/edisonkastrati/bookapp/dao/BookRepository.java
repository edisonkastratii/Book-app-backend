package com.edisonkastrati.bookapp.dao;

import com.edisonkastrati.bookapp.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
