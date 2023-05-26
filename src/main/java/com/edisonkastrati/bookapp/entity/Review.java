package com.edisonkastrati.bookapp.entity;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    @CreationTimestamp
    private Date date;

    private double rating;

    private long bookId;

    private String reviewDescription;

    public Review() {

    }
    public Review(String userEmail, Date date, double rating, long bookId, String reviewDescription) {
        this.userEmail = userEmail;
        this.date = date;
        this.rating = rating;
        this.bookId = bookId;
        this.reviewDescription = reviewDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }
}
