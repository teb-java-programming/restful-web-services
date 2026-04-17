package com.teb.practice.service;

import com.teb.practice.dto.request.GameRequest;
import com.teb.practice.dto.response.GameResponse;
import com.teb.practice.entity.Developer;
import com.teb.practice.entity.Game;
import com.teb.practice.exception.ResourceNotFoundException;
import com.teb.practice.repository.DeveloperRepository;
import com.teb.practice.repository.GameRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;

    public List<GameResponse> getGames() {

        return gameRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public GameResponse getGame(Long id) {

        return mapToResponse(findGame(id));
    }

    public GameResponse createGame(GameRequest request) {

        return mapToResponse(
                gameRepository.save(
                        Game.builder()
                                .title(request.title())
                                .genre(request.genre())
                                .developer(findDeveloper(request.developerId()))
                                .build()));
    }

    public GameResponse updateGame(Long id, GameRequest request) {

        Game game = findGame(id);

        game.setTitle(request.title());
        game.setGenre(request.genre());
        game.setDeveloper(findDeveloper(request.developerId()));

        return mapToResponse(gameRepository.save(game));
    }

    public GameResponse patchGame(Long id, GameRequest request) {

        Game game = findGame(id);

        if (request.title() != null) game.setTitle(request.title());
        if (request.genre() != null) game.setGenre(request.genre());
        if (request.developerId() != null) game.setDeveloper(findDeveloper(request.developerId()));

        return mapToResponse(gameRepository.save(game));
    }

    public void deleteGame(Long id) {

        gameRepository.delete(findGame(id));
    }

    private GameResponse mapToResponse(Game game) {

        return GameResponse.builder()
                .id(game.getId())
                .title(game.getTitle())
                .genre(game.getGenre())
                .developerId(game.getDeveloper().getId())
                .build();
    }

    private Game findGame(Long id) {

        return gameRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
    }

    private Developer findDeveloper(Long id) {

        return developerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));
    }
}
