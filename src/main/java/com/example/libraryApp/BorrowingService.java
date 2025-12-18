package com.example.libraryApp;

import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class BorrowingService {
    Map<Book, Queue<AppUser>> bookQueueMap;
    BookRepository bookRepository;
    BorrowingRepository borrowingRepository;

    public BorrowingService(BookRepository bookRepository, BorrowingRepository borrowingRepository) {
        this.bookQueueMap = new HashMap<>(); // todo update on app startup
        this.bookRepository = bookRepository;
        this.borrowingRepository = borrowingRepository;
    }

    public Map<Book, Queue<AppUser>> getBookQueueMap() {
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
            if (bookQueueMap.containsKey(book)){ // todo dont add while already in queue
                Queue<AppUser> appUserQueue = bookQueueMap.get(book);
                appUserQueue.add(appUser);
            } else {
                Queue<AppUser> appUserQueue = new PriorityQueue<>();
                appUserQueue.add(appUser);
                bookQueueMap.put(book, appUserQueue);
            }
            return BorrowingStatus.ADDED_TO_QUEUE.name();

        }
    }

    public String returnBook(AppUser appUser, Book book) throws SQLException {
        borrowingRepository.updateBorrowing(book.getId(), appUser.getId(),
                new Date(System.currentTimeMillis()), BorrowingStatus.RETURNED);

        book.isAvailable = true;
        bookRepository.update(book);

        if (bookQueueMap.containsKey(book) && !bookQueueMap.get(book).isEmpty()) {
            Queue<AppUser> appUserQueue = bookQueueMap.get(book);
            AppUser nextAppUser = appUserQueue.poll();
            System.out.println("Passing to user " + nextAppUser.getId() + " book " + book.getId());
            borrowBook(nextAppUser, book);
            return BorrowingStatus.PASSED_TO_NEXT_USER.name();
        }
        return BorrowingStatus.RETURNED.name();
    }

}
