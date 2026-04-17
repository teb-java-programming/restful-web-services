package com.teb.practice.service;

import com.teb.practice.model.Book;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class BookService {

    private static final List<Book> bookList = new ArrayList<>();

    static {
        bookList.add(new Book("B1", "The Basics of Computing", "Sripada Sharath"));
        bookList.add(new Book("B2", "OOPS! An Introduction", "Megha Barik"));
    }

    private int bookCount = bookList.size();

    public List<Book> listBooks() {

        return bookList;
    }

    public Book listBookById(String bookId) {

        for (Book bookIterator : bookList) {
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
        bookList.add(book);
        return book;
    }

    public Book updateBook(String bookId, Book book) {

        for (Book bookIterator : bookList) {
            if (bookIterator.getBookId().equalsIgnoreCase(bookId)) {
                bookIterator.setBookName(book.getBookName());
                bookIterator.setBookAuthor(book.getBookAuthor());
                return bookIterator;
            }
        }
        return null;
    }

    public Book removeBook(String bookId) {

        for (Book bookIterator : bookList) {
            if (bookIterator.getBookId().equalsIgnoreCase(bookId)) {
                bookList.remove(bookIterator);
                return bookIterator;
            }
        }
        return null;
    }
}
