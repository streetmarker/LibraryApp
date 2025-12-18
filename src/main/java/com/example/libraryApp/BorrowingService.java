package com.example.libraryApp;

import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

import org.springframework.stereotype.Service;

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

    public String borrowBook(AppUser appUser, Book book) {
        boolean isBookFree;
        try {
            isBookFree = bookRepository.isBookFree(book.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return BorrowingStatus.BORROWED.name();
        }
        if(isBookFree){
                borrowingRepository.save(new Borrowing(book.getId(), appUser.getId(),
                new Date(System.currentTimeMillis()), null, BorrowingStatus.BORROWED.name()));

                book.isAvailable = false;
                bookRepository.update(book);
                return BorrowingStatus.BORROWED_SUCCESSFULLY.name();
        } else {
            // check if already borrowed by the same user
            Borrowing existingBorrowing = borrowingRepository.findByBookId(book.getId());
            if (existingBorrowing != null && existingBorrowing.userId == appUser.getId()) {
                return BorrowingStatus.BORROWED_BY_CURRENT_USER.name();
            }

            // add to queue
            if (bookQueueMap.containsKey(book.getId())){ // todo dont add while already in queue
                Queue<Integer> appUserQueue = bookQueueMap.get(book.getId());
                appUserQueue.add(appUser.getId());
            } else {
                Queue<Integer> appUserQueue = new PriorityQueue<>();
                appUserQueue.add(appUser.getId());
                bookQueueMap.put(book.getId(), appUserQueue);
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

        book.isAvailable = true;
        bookRepository.update(book);

        if (bookQueueMap.containsKey(bookId) && !bookQueueMap.get(bookId).isEmpty()) {
            Queue<Integer> appUserQueue = bookQueueMap.get(bookId);
            Integer nextAppUserId = appUserQueue.poll();
            System.out.println("Passing to user " + nextAppUserId + " book " + bookId);
            borrowBook(appUserRepository.findById(nextAppUserId), book);
            return BorrowingStatus.PASSED_TO_NEXT_USER.name();
        }
        return BorrowingStatus.RETURNED_SUCCESSFULLY.name();
    }

}
