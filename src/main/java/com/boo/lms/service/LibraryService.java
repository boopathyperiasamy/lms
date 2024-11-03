package com.boo.lms.service;

import com.boo.lms.Exception.BorrowFailure;
import com.boo.lms.model.Book;

import java.util.List;
import java.util.Map;

public interface LibraryService {
    Book addBook(Book book);
    Book removeBook(String isbn);
    Book findBookByISBN(String isbn);
    List<Book> findByAuthor(String author);
    boolean borrowBook(String isbn) throws BorrowFailure;
    boolean returnBook(String isbn);

    Map<String, Integer> getStockLevel();
}
