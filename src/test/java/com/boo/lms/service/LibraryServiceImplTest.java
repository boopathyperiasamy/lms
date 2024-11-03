package com.boo.lms.service;

import com.boo.lms.Exception.BorrowFailure;
import com.boo.lms.Exception.ReturnFailure;
import com.boo.lms.model.Book;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceImplTest {

    public static final String ISBN = "2334-3434-3433";
    Book book = Book.builder().isbn("2334-3434-3433").title("The Stars").author("James Bat").availableCopies(1).build();
    LibraryService service = new LibraryServiceImpl();

    @Test
    void addBook() {
        service.addBook(book);
        assertEquals(book, service.findBookByISBN("2334-3434-3433"));
    }

    @Test
    void removeBook() {
        service.addBook(book);
        assertEquals(book, service.findBookByISBN(ISBN));
        service.removeBook(book.getIsbn());
        assertNull(service.findBookByISBN(ISBN));
    }

    @Test
    void findByAuthor() {
        service.addBook(book);
        assertEquals(List.of(book), service.findByAuthor("James Bat"));
    }

    @Test
    void borrowBook() {
        assertEquals(Map.of(), service.getStockLevel());
        service.addBook(book);
        service.borrowBook(book.getIsbn());
        assertEquals(Map.of(ISBN, 1), service.getStockLevel());
    }

    @Test
    void borrowBookMoreThanAvailableShouldThrowException() {
        assertEquals(Map.of(), service.getStockLevel());
        service.addBook(book);
        service.borrowBook(book.getIsbn());
        assertEquals(Map.of(ISBN, 1), service.getStockLevel());

        assertThrows(BorrowFailure.class, () -> {
            service.borrowBook(book.getIsbn());
        });
    }

    @Test
    void returnBook() {
        assertEquals(Map.of(), service.getStockLevel());
        service.addBook(book);
        service.borrowBook(book.getIsbn());
        assertEquals(Map.of(ISBN, 1), service.getStockLevel());
        service.returnBook(book.getIsbn());
        assertEquals(Map.of(ISBN, 0), service.getStockLevel());
    }

    @Test
    void returnBookOnNoBorrowShouldThrowException() {
        assertEquals(Map.of(), service.getStockLevel());
        service.addBook(book);
        service.borrowBook(book.getIsbn());
        assertEquals(Map.of(ISBN, 1), service.getStockLevel());
        service.returnBook(book.getIsbn());
        assertEquals(Map.of(ISBN, 0), service.getStockLevel());
        assertThrows(ReturnFailure.class, () -> {
            service.returnBook(book.getIsbn());
        });
    }

}