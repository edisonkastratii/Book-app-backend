package com.edisonkastrati.bookapp.service;

import com.edisonkastrati.bookapp.repository.BookRepository;
import com.edisonkastrati.bookapp.repository.CheckoutRepository;
import com.edisonkastrati.bookapp.repository.HistoryRepository;
import com.edisonkastrati.bookapp.entity.Book;
import com.edisonkastrati.bookapp.entity.Checkout;
import com.edisonkastrati.bookapp.entity.History;
import com.edisonkastrati.bookapp.responseModels.ShelfCurrentLoansResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;
    private CheckoutRepository checkoutRepository;
    private HistoryRepository historyRepository;

    @Autowired
    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository, HistoryRepository historyRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception{
        Optional<Book> book = bookRepository.findById(bookId);
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0){
            throw new Exception("Book doesn't exist or already checkout by the user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() -1);
        bookRepository.save(book.get());

        Checkout checkout = new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId()
        );

        checkoutRepository.save(checkout);
        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId){
        Checkout checkout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(checkout != null){
            return true;
        }else {
            return false;
        }
    }

    public int currentLoansCount(String userEmail){
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception{
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();
        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        for(Checkout i: checkoutList){
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookRepository.findByBookIds(bookIdList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

        for(Book book: books){
            Optional<Checkout> checkout = checkoutList
                    .stream()
                    .filter(x -> x.getBookId() == book.getId())
                    .findFirst();
            if(checkout.isPresent()){
                Date d1 = sdf.parse(checkout.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());
                TimeUnit time = TimeUnit.DAYS;
                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);
                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) difference_In_Time));
            }
        }
        return shelfCurrentLoansResponses;
    }

    public void returnBook(String userEmail, Long bookId)throws Exception{
        Optional<Book> book = bookRepository.findById(bookId);
        Checkout checkout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(!book.isPresent() || checkout == null){
            throw new Exception("Book doesn't exist or not checked by the user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookRepository.save(book.get());
        checkoutRepository.deleteById(checkout.getId());

        History history = new History(
                userEmail,
                checkout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
        );

        historyRepository.save(history);
    }

    public void renewLoan(String userEmail, Long bookId)throws Exception{
        Checkout checkout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(checkout == null){
            throw new Exception("Book does not exist or not checked by the user");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

        Date d1 = sdf.parse(checkout.getReturnDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        if(d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0){
            checkout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(checkout);
        }
    }
}
