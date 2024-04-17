package com.example.crudrestapplication.controller;

import com.example.crudrestapplication.dto.BookRequest;
import com.example.crudrestapplication.model.Book;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/book")
public class BookController {
    private final View error;
    private ConcurrentHashMap<String, Book> books;
    public BookController(View error) {
        books = new ConcurrentHashMap<>();
        books.put("0X-13", new Book("0X-13", "Gone with the wind", "Shakpeare", 1865));
        books.put("0X-14", new Book("0X-14", "Harry Potter and the Philosopher's Stone", "J.K.Rowling", 1995));
        this.error = error;
    }
    //List book
    @GetMapping()
    public List<Book> getBook() {
        return books.values().stream().toList();
    }
    //Create Book
    @PostMapping
    public Book createNewBook(@RequestBody BookRequest bookRequest) {
    String uuid = UUID.randomUUID().toString();
    Book newBook = new Book(uuid, bookRequest.title(), bookRequest.author(), bookRequest.year());
    books.put(uuid, newBook);
    return newBook;
    }
    //Search book by ID
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable("id") String id) {
        return books.get(id);
    }
    //Update Book information
    @PutMapping("/{id}")
    public void UpdateBookById(@PathVariable("id") String id, @RequestBody BookRequest bookRequest, HttpServletResponse response) throws IOException {
        if (books.containsKey(id)) {
            Book updateBook = new Book(id, bookRequest.title(), bookRequest.author(), bookRequest.year());
            books.replace(id, updateBook);
            String Message = "Book with ID: " + id + " update sucessfully.";
            response.getWriter().write(Message);
        } else {
            String errorMessage = "Book with ID: " + id + " does not exist.";
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(errorMessage);
        }
    }
    //Delete book by ID
    @DeleteMapping("/{id}")
    public Book delBookById(@PathVariable("id") String id) {
        return books.remove(id);
    }
}
