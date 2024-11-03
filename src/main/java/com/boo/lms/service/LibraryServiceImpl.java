package com.boo.lms.service;

import com.boo.lms.Exception.BorrowFailure;
import com.boo.lms.Exception.ReturnFailure;
import com.boo.lms.model.Book;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@NoArgsConstructor
public class LibraryServiceImpl implements LibraryService{

    ConcurrentHashMap<String, Book> bookByIsbn = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Book> frequentlyAccessedBooks = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Integer> booksOnLoan = new ConcurrentHashMap<>();

    @Override
    public Book addBook(Book book) {
        log.info("Adding new book to library - {}",book);
        bookByIsbn.put(book.getIsbn(), book);

        return book;
    }

    @Override
    public Book removeBook(String isbn) {
        log.info("Removed book from library - {}",isbn);
        var removedBook = bookByIsbn.remove(isbn);

        //remove from frequently accessed
        if (removedBook != null) {
            frequentlyAccessedBooks.remove(removedBook.getIsbn());
        }
        return removedBook;
    }

    @Override
    public Book findBookByISBN(String isbn) {
        return bookByIsbn.get(isbn);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookByIsbn.values().stream().filter(b -> author.equalsIgnoreCase(b.getAuthor())).toList();
    }

    @Override
    public boolean borrowBook(String isbn) throws BorrowFailure {
        //compute method helps to keep all the operation thread safe and concurrent
        var borrowedBook = bookByIsbn.computeIfPresent(isbn, (k,v) -> {
                booksOnLoan.compute(isbn, (x, y) -> {
                    if ( v.getAvailableCopies() == 0 || (y != null && v.getAvailableCopies() == y)){
                        throw new BorrowFailure("Sorry, Currently no copies available to borrow");
                    } else {
                        return (y != null ? y : 0) + 1;
                    }
                });
                return v;
        });

        if (borrowedBook == null){
            throw new BorrowFailure("Book with isbn "+isbn+" not found");
        } else {
            log.info("Borrowing book - {}",isbn);
            return true;
        }
    }

    @Override
    public boolean returnBook(String isbn) {
        return booksOnLoan.computeIfPresent(isbn, (k,v) -> {
           if (v == 0){
               throw new ReturnFailure("Sorry, do not see a borrow on this book");
           }
            log.info("Returning book - {}",isbn);
           return v - 1;
        }) != null;
    }

    @Override
    public Map<String, Integer> getStockLevel() {
        return new HashMap<>(booksOnLoan);
    }
}
