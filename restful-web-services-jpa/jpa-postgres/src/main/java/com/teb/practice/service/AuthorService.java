package com.teb.practice.service;

import com.teb.practice.dto.request.AuthorRequest;
import com.teb.practice.dto.response.AuthorResponse;
import com.teb.practice.dto.response.BookResponse;
import com.teb.practice.entity.Author;
import com.teb.practice.exception.ResourceNotFoundException;
import com.teb.practice.repository.AuthorRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorResponse> getAuthors() {

        return authorRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public AuthorResponse getAuthor(Long id) {

        return mapToResponse(findAuthor(id));
    }

    public AuthorResponse createAuthor(AuthorRequest request) {

        return mapToResponse(
                authorRepository.save(
                        Author.builder().name(request.name()).email(request.email()).build()));
    }

    public AuthorResponse updateAuthor(Long id, AuthorRequest request) {

        Author author = findAuthor(id);

        author.setName(request.name());
        author.setEmail(request.email());

        return mapToResponse(authorRepository.save(author));
    }

    public void deleteAuthor(Long id) {

        authorRepository.delete(findAuthor(id));
    }

    private AuthorResponse mapToResponse(Author author) {

        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .books(getListOfBooks(author))
                .build();
    }

    private Author findAuthor(Long id) {

        return authorRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
    }

    private List<BookResponse> getListOfBooks(Author author) {

        return author.getBooks().stream()
                .map(
                        book ->
                                BookResponse.builder()
                                        .id(book.getId())
                                        .title(book.getTitle())
                                        .isbn(book.getIsbn())
                                        .authorId(author.getId())
                                        .build())
                .toList();
    }
}
