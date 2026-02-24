package com.teb.practice.service;

import com.teb.practice.model.Book;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class BookService {

    private static final List<Book> BOOK_LIST = new ArrayList<>();

    static {
        BOOK_LIST.add(new Book("B1", "The Basics of Computing", "Sripada Sharath"));
        BOOK_LIST.add(new Book("B2", "OOPS! An Introduction", "Megha Barik"));
    }

    private int bookCount = BOOK_LIST.size();

    public List<Book> listBooks() {

        return BOOK_LIST;
    }

    public Book listBookById(String bookId) {

        for (Book bookIterator : BOOK_LIST) {
            if (bookIterator.getBookId().equalsIgnoreCase(bookId)) {
                return bookIterator;
            }
        }
        return null;
    }

    public Book addBook(Book book) {

        if (book.getBookId() == null) {
            book.setBookId("B".concat(String.valueOf(++bookCount)));
        }
        BOOK_LIST.add(book);
        return book;
    }

    public Book updateBook(String bookId, Book book) {

        for (Book bookIterator : BOOK_LIST) {
            if (bookIterator.getBookId().equalsIgnoreCase(bookId)) {
                bookIterator.setBookName(book.getBookName());
                bookIterator.setBookAuthor(book.getBookAuthor());
                return bookIterator;
            }
        }
        return null;
    }

    public Book removeBook(String bookId) {

        for (Book bookIterator : BOOK_LIST) {
            if (bookIterator.getBookId().equalsIgnoreCase(bookId)) {
                BOOK_LIST.remove(bookIterator);
                return bookIterator;
            }
        }
        return null;
    }
}
