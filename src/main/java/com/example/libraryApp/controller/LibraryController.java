package com.example.libraryApp.controller;

import java.util.Map;
import java.util.Queue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.libraryApp.constans.BorrowingStatus;
import com.example.libraryApp.model.RestRequest;
import com.example.libraryApp.repository.AppUserRepository;
import com.example.libraryApp.repository.BookRepository;
import com.example.libraryApp.service.BorrowingService;

@RestController
@RequestMapping("/api")
public class LibraryController {

    BookRepository BookRepository;
    BorrowingService BorrowingService;
    AppUserRepository AppUserRepository;

    private LibraryController(BookRepository bookRepository, BorrowingService borrowingService, AppUserRepository appUserRepository) {
        this.BookRepository = bookRepository;
        this.BorrowingService = borrowingService;
        this.AppUserRepository = appUserRepository;
    }

    @GetMapping("/books")
    public ResponseEntity<String> books() {
        return ResponseEntity.ok(BookRepository.findAll().toString());
    }

    @GetMapping("/users")
    public ResponseEntity<String> users() {
        return ResponseEntity.ok(AppUserRepository.findAll().toString());
    }

    @GetMapping("/queueMap")
    public ResponseEntity<Map<Integer, Queue<Integer>>> queueMap() {
        return ResponseEntity.ok(BorrowingService.getBookQueueMap());
    }

    @PostMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestBody RestRequest restRequest) {
        BookRepository.save(restRequest.getBook());
        return ResponseEntity.ok(restRequest.getBook().toString());
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody RestRequest restRequest) {
        AppUserRepository.save(restRequest.getAppUser());
        return ResponseEntity.ok("User added successfully");
    }

    @PostMapping("/borrowBook")
    public ResponseEntity<String> borrowBook(@RequestBody RestRequest restRequest) {
        try {
            String borrowed = BorrowingService.borrowBook(restRequest.getAppUserId(), restRequest.getBookId());
            if (borrowed.equals(BorrowingStatus.BORROWED_SUCCESSFULLY.name())) {
                return ResponseEntity.ok("Book borrowed successfully");
            } else if (borrowed.equals(BorrowingStatus.BORROWED_BY_CURRENT_USER.name())) {
                return ResponseEntity.ok("Book already borrowed by current user");
            } else if (borrowed.equals(BorrowingStatus.ADDED_TO_QUEUE.name())) {
                return ResponseEntity.status(400).body("Book is not available for borrowing, added to waitlist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error borrowing book");
        }
        return null;
    }

    @PostMapping("/returnBook")
    public ResponseEntity<String> returnBook(@RequestBody RestRequest restRequest) {
        try {
            String returned = BorrowingService.returnBook(restRequest.getAppUserId(), restRequest.getBookId());
            if (returned.equals(BorrowingStatus.RETURNED_SUCCESSFULLY.name())) {
                return ResponseEntity.ok("Book returned successfully");
            } else if (returned.equals(BorrowingStatus.PASSED_TO_NEXT_USER.name())) {
                return ResponseEntity.ok("Book returned successfully, passed to next user in queue");
            } else if (returned.equals("BOOK_NOT_BORROWED_BY_USER")) {
                return ResponseEntity.status(400).body("Book was not borrowed by this user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error returning book");
        }
        return null;
    }    
}
