package com.example.libraryApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.libraryApp.model.AppUser;
import com.example.libraryApp.model.Book;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryAppApplicationTests {

	@Autowired
    TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void userCanBorrowBookWhenAvailable() {
	    Book book = new Book("1984", 1948, "George Orwell");
	    AppUser user = new AppUser("Alice", "Smith", 1990);

	    // try {
	    //     boolean success = borrowingService.borrowBook(user, book);
	    //     assert(success);
	    // } catch (Exception e) {
	    //     assert(false);
	    // }

	}

}
