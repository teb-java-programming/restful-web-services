package com.teb.practice.service;

import static java.lang.System.currentTimeMillis;

import com.teb.practice.dto.request.BookRequest;
import com.teb.practice.dto.response.BookResponse;
import com.teb.practice.entity.Author;
import com.teb.practice.entity.Book;
import com.teb.practice.exception.ResourceNotFoundException;
import com.teb.practice.repository.AuthorRepository;
import com.teb.practice.repository.BookRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookResponse> getBooks() {

        return bookRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public BookResponse getBook(Long id) {

        return mapToResponse(findBook(id));
    }

    public BookResponse createBook(BookRequest request) {

        return mapToResponse(
                bookRepository.save(
                        Book.builder()
                                .title(request.title())
                                .isbn("ISBN-" + currentTimeMillis())
                                .author(findAuthor(request.authorId()))
                                .build()));
    }

    public BookResponse updateBook(Long id, BookRequest request) {

        Book book = findBook(id);

        book.setTitle(request.title());
        book.setIsbn("ISBN-" + currentTimeMillis());
        book.setAuthor(findAuthor(request.authorId()));

        return mapToResponse(bookRepository.save(book));
    }

    public void deleteBook(Long id) {

        bookRepository.delete(findBook(id));
    }

    private BookResponse mapToResponse(Book book) {

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .authorId(book.getAuthor().getId())
                .build();
    }

    private Book findBook(Long id) {

        return bookRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    private Author findAuthor(Long id) {

        return authorRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
    }
}
