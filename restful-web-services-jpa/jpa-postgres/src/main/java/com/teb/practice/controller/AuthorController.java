package com.teb.practice.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.teb.practice.dto.request.AuthorRequest;
import com.teb.practice.dto.response.AuthorResponse;
import com.teb.practice.response.ApiResponse;
import com.teb.practice.service.AuthorService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ApiResponse<List<AuthorResponse>> getAuthors() {

        return new ApiResponse<>("Author list", authorService.getAuthors());
    }

    @GetMapping("/{id}")
    public ApiResponse<AuthorResponse> getAuthor(@PathVariable Long id) {

        return new ApiResponse<>("Author found", authorService.getAuthor(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuthorResponse>> createAuthor(
            @Valid @RequestBody AuthorRequest request) {

        return status(201)
                .body(new ApiResponse<>("Author created", authorService.createAuthor(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> updateAuthor(
            @PathVariable Long id, @RequestBody AuthorRequest request) {

        return ok(new ApiResponse<>("Author updated", authorService.updateAuthor(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAuthor(@PathVariable Long id) {

        authorService.deleteAuthor(id);

        return ok(new ApiResponse<>("Author deleted", null));
    }
}
