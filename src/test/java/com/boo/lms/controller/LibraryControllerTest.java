package com.boo.lms.controller;

import com.boo.lms.model.Book;
import com.boo.lms.service.LibraryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LibraryControllerTest {

    public static final String ISBN = "2334-3434-3433";
    Book book = Book.builder().isbn("2334-3434-3433").title("The Stars").author("James Bat").publicationYear(2020).availableCopies(100).build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibraryService service;

    String bookJson = null;

    @BeforeAll
    public void init() throws JsonProcessingException {
        bookJson = objectMapper.writeValueAsString(book);
    }

    @Test
    void addBookShouldReturnBookAdded() throws Exception {
        when(service.addBook(book)).thenReturn(book);
        mockMvc.perform(post("/library").contentType("application/json").content(bookJson)).andExpect(status().isOk())
                .andExpect(content().string(bookJson));
    }

    @Test
    void removeBook() throws Exception {
        when(service.removeBook(ISBN)).thenReturn(book);
        mockMvc.perform(delete("/library/isbn/"+ISBN))
                .andExpect(status().isOk())
                .andExpect(content().string(bookJson));
    }

    @Test
    void findBookByISBN() throws Exception {
        when(service.findBookByISBN(ISBN)).thenReturn(book);
        mockMvc.perform(get("/library/isbn/"+ISBN))
                .andExpect(status().isOk())
                .andExpect(content().string(bookJson));
    }

    @Test
    void findByAuthor() throws Exception {
        when(service.findByAuthor("James Bat")).thenReturn(List.of(book));
        mockMvc.perform(get("/library/author/James Bat"))
                .andExpect(status().isOk())
                .andExpect(content().string("["+bookJson+"]"));
    }

    @Test
    void borrowBook() throws Exception {
        when(service.borrowBook(ISBN)).thenReturn(true);
        mockMvc.perform(put("/library/borrow/"+ISBN))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void returnBook() throws Exception {
        when(service.returnBook(ISBN)).thenReturn(true);
        mockMvc.perform(put("/library/return/"+ISBN))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getStockLevel() throws Exception {
        when(service.getStockLevel()).thenReturn(Map.of(ISBN, 2));
        mockMvc.perform(get("/library/stocklevel"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"2334-3434-3433\":2}"));
    }
}