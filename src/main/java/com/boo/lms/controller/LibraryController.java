package com.boo.lms.controller;

import com.boo.lms.model.Book;
import com.boo.lms.service.LibraryService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping
    public Book addBook(@Valid @RequestBody Book book){
        return libraryService.addBook(book);
    }

    @DeleteMapping("/isbn/{isbn}")
    public Book removeBook(@PathVariable @NotBlank String isbn){
        return libraryService.removeBook(isbn);
    }

    @GetMapping("/isbn/{isbn}")
    public Book findBookByISBN(@PathVariable @NotBlank String isbn){
        return libraryService.findBookByISBN(isbn);
    }

    @GetMapping("/author/{author}")
    List<Book> findByAuthor(@PathVariable @NotBlank String author){
        return libraryService.findByAuthor(author);
    }

    @PutMapping("/borrow/{isbn}")
    @RateLimiter(name="library-service")
    public boolean borrowBook(@PathVariable @NotBlank String isbn) {
        return libraryService.borrowBook(isbn);
    }

    @PutMapping("/return/{isbn}")
    @RateLimiter(name="library-service")
    public boolean returnBook(@PathVariable String isbn){
        return libraryService.returnBook(isbn);
    }

    @GetMapping("/stocklevel")
    public Map<String, Integer> getStockLevel(){
        return libraryService.getStockLevel();
    }
}
