package com.teb.practice.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Book {

    private String bookId;

    @NotNull
    @Size(min = 10, max = 40, message = "Book name should be between 10 to 40 characters.")
    private String bookName;

    @NotNull
    @Size(min = 10, max = 20, message = "Author name should be between 10 to 20 characters.")
    private String bookAuthor;

    /* For early versions of Spring-boot, add a manual no-argument constructor */
    public Book(String bookId, String bookName, String bookAuthor) {
        super();
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    @Override
    public String toString() {
        return "Book [bookId="
                + bookId
                + ", bookName="
                + bookName
                + ", bookAuthor="
                + bookAuthor
                + "]";
    }
}
