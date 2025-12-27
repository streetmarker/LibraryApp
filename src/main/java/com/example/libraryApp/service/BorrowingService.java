package com.example.libraryApp.service;

import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

import org.springframework.stereotype.Service;

import com.example.libraryApp.constans.BorrowingStatus;
import com.example.libraryApp.model.Book;
import com.example.libraryApp.model.Borrowing;
import com.example.libraryApp.repository.AppUserRepository;
import com.example.libraryApp.repository.BookRepository;
import com.example.libraryApp.repository.BorrowingRepository;

@Service
public class BorrowingService {

    Map<Integer, Queue<Integer>> bookQueueMap;
    BookRepository bookRepository;
    BorrowingRepository borrowingRepository;
    AppUserRepository appUserRepository;

    public BorrowingService(BookRepository bookRepository, BorrowingRepository borrowingRepository, AppUserRepository appUserRepository) {
        this.bookQueueMap = new HashMap<>(); // todo update on app startup
        this.bookRepository = bookRepository;
        this.borrowingRepository = borrowingRepository;
        this.appUserRepository = appUserRepository;
    }

    public Map<Integer, Queue<Integer>> getBookQueueMap() {
        return bookQueueMap;
    }

    public String borrowBook(int appUserId, int bookId) {
        boolean isBookFree;
        try {
            isBookFree = bookRepository.isBookFree(bookId);
        } catch (Exception e) {
            e.printStackTrace();
            return BorrowingStatus.BORROWED.name();
        }
        if(isBookFree){
                borrowingRepository.save(new Borrowing(bookId, appUserId,
                new Date(System.currentTimeMillis()), null, BorrowingStatus.BORROWED.name()));
                
                Book book = bookRepository.findById(bookId);
                book.setIsAvailable(false);
                bookRepository.update(book);
                return BorrowingStatus.BORROWED_SUCCESSFULLY.name();
        } else {
            // check if already borrowed by the same user
            Borrowing existingBorrowing = borrowingRepository.findByBookId(bookId);
            if (existingBorrowing != null && existingBorrowing.getAppUserId() == appUserId) {
                return BorrowingStatus.BORROWED_BY_CURRENT_USER.name();
            }

            // add to queue
            if (bookQueueMap.containsKey(bookId)){ // todo dont add while already in queue
                Queue<Integer> appUserQueue = bookQueueMap.get(bookId);
                appUserQueue.add(appUserId);
            } else {
                Queue<Integer> appUserQueue = new PriorityQueue<>();
                appUserQueue.add(appUserId);
                bookQueueMap.put(bookId, appUserQueue);
            }
            return BorrowingStatus.ADDED_TO_QUEUE.name();

        }
    }

    public String returnBook(int appUserId, int bookId) throws SQLException {

        if(!borrowingRepository.checkBorrowingStatus(bookId, appUserId).equals(BorrowingStatus.BORROWED.name())) {
            return "BOOK_NOT_BORROWED_BY_USER";
        }

        borrowingRepository.updateBorrowing(bookId, appUserId,
                new Date(System.currentTimeMillis()), BorrowingStatus.RETURNED);
        Book book = bookRepository.findById(bookId);

        book.setIsAvailable(true);
        bookRepository.update(book);

        if (bookQueueMap.containsKey(bookId) && !bookQueueMap.get(bookId).isEmpty()) {
            Queue<Integer> appUserQueue = bookQueueMap.get(bookId);
            Integer nextAppUserId = appUserQueue.poll();
            System.out.println("Passing to user " + nextAppUserId + " book " + bookId);
            borrowBook(nextAppUserId, bookId);
            return BorrowingStatus.PASSED_TO_NEXT_USER.name();
        }
        return BorrowingStatus.RETURNED_SUCCESSFULLY.name();
    }

}
