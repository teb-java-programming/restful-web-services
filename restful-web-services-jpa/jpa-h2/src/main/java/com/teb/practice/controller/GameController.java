package com.teb.practice.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.teb.practice.dto.request.GameRequest;
import com.teb.practice.dto.response.GameResponse;
import com.teb.practice.response.ApiResponse;
import com.teb.practice.service.GameService;

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
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public ApiResponse<List<GameResponse>> getGames() {

        return new ApiResponse<>("Game list", gameService.getGames());
    }

    @GetMapping("/{id}")
    public ApiResponse<GameResponse> getGame(@PathVariable Long id) {

        return new ApiResponse<>("Game found", gameService.getGame(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GameResponse>> createGame(
            @Valid @RequestBody GameRequest request) {

        return status(201).body(new ApiResponse<>("Game created", gameService.createGame(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GameResponse>> updateGame(
            @PathVariable Long id, @RequestBody GameRequest request) {

        return ok(new ApiResponse<>("Game updated", gameService.updateGame(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<GameResponse>> patchGame(
            @PathVariable Long id, @RequestBody GameRequest request) {

        return ok(new ApiResponse<>("Game updated", gameService.patchGame(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGame(@PathVariable Long id) {

        gameService.deleteGame(id);

        return ok(new ApiResponse<>("Game deleted", null));
    }
}
