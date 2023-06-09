package com.edisonkastrati.bookapp.dao;

import com.edisonkastrati.bookapp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
