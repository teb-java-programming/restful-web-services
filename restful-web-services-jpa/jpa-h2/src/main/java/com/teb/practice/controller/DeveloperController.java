package com.teb.practice.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.teb.practice.dto.request.DeveloperRequest;
import com.teb.practice.dto.response.DeveloperResponse;
import com.teb.practice.response.ApiResponse;
import com.teb.practice.service.DeveloperService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;

    @GetMapping
    public ApiResponse<List<DeveloperResponse>> getDevelopers() {

        return new ApiResponse<>("Developer list", developerService.getDevelopers());
    }

    @GetMapping("/{id}")
    public ApiResponse<DeveloperResponse> getDeveloper(@PathVariable Long id) {

        return new ApiResponse<>("Developer found", developerService.getDeveloper(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeveloperResponse>> createDeveloper(
            @Valid @RequestBody DeveloperRequest request) {

        return status(201)
                .body(
                        new ApiResponse<>(
                                "Developer created", developerService.createDeveloper(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeveloperResponse>> updateDeveloper(
            @PathVariable Long id, @RequestBody DeveloperRequest request) {

        return ok(
                new ApiResponse<>(
                        "Developer updated", developerService.updateDeveloper(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DeveloperResponse>> patchDeveloper(
            @PathVariable Long id, @RequestBody DeveloperRequest request) {

        return ok(
                new ApiResponse<>(
                        "Developer updated", developerService.patchDeveloper(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDeveloper(@PathVariable Long id) {

        developerService.deleteDeveloper(id);

        return ok(new ApiResponse<>("Developer deleted", null));
    }
}
