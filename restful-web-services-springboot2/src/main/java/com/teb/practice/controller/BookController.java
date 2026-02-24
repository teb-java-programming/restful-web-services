package com.teb.practice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.teb.practice.exception.BookNotFoundException;
import com.teb.practice.model.Book;
import com.teb.practice.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

@RestController
public class BookController {

    private static final String BOOK_ID = "Book ID: ";

    @Autowired BookService bookService;

    @Autowired private MessageSource messageSource;

    @GetMapping("/")
    public String welcomeMessage() {

        return messageSource.getMessage("welcome.message", null, LocaleContextHolder.getLocale());
    }

    @GetMapping("/books")
    public List<Book> getBookList() {

        return bookService.listBooks();
    }

    @GetMapping("/books/{bookId}")
    public EntityModel<Book> getBookById(@PathVariable String bookId) {

        Book bookToLookup = bookService.listBookById(bookId);
        if (bookToLookup == null) {
            throw new BookNotFoundException(BOOK_ID.concat(bookId.toUpperCase()));
        }

        /*
         * HATEOAS (Hypermedia As The Engine Of Application State) - used to add dynamic
         * links to a response body
         */
        EntityModel<Book> bookResource = EntityModel.of(bookToLookup);

        /* Defining the link to be displayed */
        WebMvcLinkBuilder newLink = linkTo(methodOn(this.getClass()).getBookList());
        bookResource.add(newLink.withRel("Check out all books"));

        return bookResource;
    }

    @PostMapping("/books")
    public ResponseEntity<Object> addNewBook(@Valid @RequestBody Book book) {

        Book newBook = bookService.addBook(book);

        URI pathLocation =
                ServletUriComponentsBuilder.fromCurrentRequest() // fetches current path
                        .path("/{bookId}") // appends required path
                        .buildAndExpand(newBook.getBookId())
                        .toUri(); // replace dynamic value and create new path

        /* Returns a HTTP code while adding a new record */
        return ResponseEntity.created(pathLocation).build();
    }

    @PatchMapping("/books/{bookId}")
    public Book updateOldBook(@PathVariable String bookId, @RequestBody Book book) {

        Book bookToUpdate = bookService.updateBook(bookId, book);
        if (bookToUpdate == null) {
            throw new BookNotFoundException(BOOK_ID.concat(bookId.toUpperCase()));
        }
        return bookToUpdate;
    }

    @DeleteMapping("/books/{bookId}")
    public Book deleteOldBook(@PathVariable String bookId) {

        Book bookToDelete = bookService.removeBook(bookId);
        if (bookToDelete == null) {
            throw new BookNotFoundException(BOOK_ID.concat(bookId.toUpperCase()));
        }
        return bookToDelete;
    }
}
