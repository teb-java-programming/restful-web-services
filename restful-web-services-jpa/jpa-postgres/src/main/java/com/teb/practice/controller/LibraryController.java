package com.teb.practice.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.teb.practice.dto.request.LibraryRequest;
import com.teb.practice.dto.response.LibraryResponse;
import com.teb.practice.response.ApiResponse;
import com.teb.practice.service.LibraryService;

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
@RequestMapping("/api/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping
    public ApiResponse<List<LibraryResponse>> getLibraries() {

        return new ApiResponse<>("Library list", libraryService.getLibraries());
    }

    @GetMapping("/{id}")
    public ApiResponse<LibraryResponse> getLibrary(@PathVariable Long id) {

        return new ApiResponse<>("Library found", libraryService.getLibrary(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LibraryResponse>> createLibrary(
            @Valid @RequestBody LibraryRequest request) {

        return status(201)
                .body(new ApiResponse<>("Library created", libraryService.createLibrary(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LibraryResponse>> updateLibrary(
            @PathVariable Long id, @RequestBody LibraryRequest request) {

        return ok(new ApiResponse<>("Library updated", libraryService.updateLibrary(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLibrary(@PathVariable Long id) {

        libraryService.deleteLibrary(id);

        return ok(new ApiResponse<>("Library deleted", null));
    }
}
