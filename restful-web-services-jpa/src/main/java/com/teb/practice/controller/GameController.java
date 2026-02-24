package com.teb.practice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.teb.practice.exception.DeveloperNotFoundException;
import com.teb.practice.exception.GameNotFoundException;
import com.teb.practice.model.Developer;
import com.teb.practice.model.Game;
import com.teb.practice.service.DeveloperRepository;
import com.teb.practice.service.GameRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class GameController {

  @Autowired GameRepository gameRepository;

  @Autowired DeveloperRepository developerRepository;

  @GetMapping("/")
  public String welcomeMessage() {
    return "Hello!";
  }

  @GetMapping("/developers")
  public List<Developer> getDeveloperList() {
    return developerRepository.findAll();
  }

  @GetMapping("/games")
  public List<Game> getGameList() {
    return gameRepository.findAll();
  }

  /* Get developers by developerId */
  @GetMapping("/developers/{developerId}")
  public EntityModel<Optional<Developer>> getDeveloperById(@PathVariable String developerId) {
    Optional<Developer> developerToLookup = developerRepository.findById(developerId);
    if (developerToLookup.isEmpty()) {
      throw new DeveloperNotFoundException("Developer Id: ".concat(developerId.toUpperCase()));
    }

    EntityModel<Optional<Developer>> developerResource = EntityModel.of(developerToLookup);
    WebMvcLinkBuilder newLink = linkTo(methodOn(this.getClass()).getDeveloperList());
    developerResource.add(newLink.withRel("Check out all developers"));

    return developerResource;
  }

  /* Get games by gameId */
  @GetMapping("/games/{gameId}")
  public EntityModel<Optional<Game>> getGameById(@PathVariable String gameId) {
    Optional<Game> gameToLookup = gameRepository.findById(gameId);
    if (gameToLookup.isEmpty()) {
      throw new GameNotFoundException("Game Id: ".concat(gameId.toUpperCase()));
    }

    EntityModel<Optional<Game>> gameResource = EntityModel.of(gameToLookup);
    WebMvcLinkBuilder newLink = linkTo(methodOn(this.getClass()).getGameList());
    gameResource.add(newLink.withRel("Check out all games"));

    return gameResource;
  }

  /* Get games by developerId */
  @GetMapping("/developers/{developerId}/games")
  public Set<Game> getGameByDeveloper(@PathVariable String developerId) {
    Optional<Developer> developerToLookup = developerRepository.findById(developerId);
    if (developerToLookup.isEmpty()) {
      throw new DeveloperNotFoundException("Developer Id: ".concat(developerId.toUpperCase()));
    }

    return developerToLookup.get().getGameSet();
  }

  @PostMapping("/developers")
  public ResponseEntity<Object> addNewDeveloper(@Valid @RequestBody Developer developer) {
    Developer newDeveloper = developerRepository.save(developer);

    URI pathLocation =
        ServletUriComponentsBuilder.fromCurrentRequest() // fetches current path
            .path("/{developerId}") // appends required path
            .buildAndExpand(newDeveloper.getDeveloperId())
            .toUri(); // replace dynamic value and create new path

    /* Returns a HTTP code while adding a new record */
    return ResponseEntity.created(pathLocation).build();
  }

  /* Add game by developerId */
  @PostMapping("/developers/{developerId}/games")
  public ResponseEntity<Object> addNewGame(
      @PathVariable String developerId, @RequestBody Game game) {
    Optional<Developer> developerToLookup = developerRepository.findById(developerId);
    if (developerToLookup.isEmpty()) {
      throw new DeveloperNotFoundException("Developer Id: ".concat(developerId.toUpperCase()));
    }

    game.setGameDeveloper(developerToLookup.get());
    Game newGame = gameRepository.save(game);

    URI pathLocation =
        ServletUriComponentsBuilder.fromCurrentRequest() // fetches current path
            .path("/{developerId}") // appends required path
            .buildAndExpand(newGame.getGameId())
            .toUri(); // replace dynamic value and create new path

    /* Returns a HTTP code while adding a new record */
    return ResponseEntity.created(pathLocation).build();
  }

  @PatchMapping("/developers")
  public void updateDeveloper(@RequestBody Developer developer) {
    Optional<Developer> developerToLookup =
        developerRepository.findById(developer.getDeveloperId());
    if (developerToLookup.isEmpty()) {
      throw new DeveloperNotFoundException(
          "Developer Id: ".concat(developer.getDeveloperId().toUpperCase()));
    } else {
      Developer developerToUpdate = developerToLookup.get();
      developerToUpdate.setDeveloperName(developer.getDeveloperName());
      developerRepository.saveAndFlush(developerToUpdate);
    }
  }

  /* Update game by developerId */
  @PatchMapping("/developers/{developerId}/games")
  public void updateGame(@PathVariable String developerId, @RequestBody Game game) {
    Optional<Developer> developerToLookup = developerRepository.findById(developerId);
    boolean gameToLookup = gameRepository.existsById(game.getGameId());
    if (developerToLookup.isEmpty()) {
      throw new DeveloperNotFoundException("Developer Id: ".concat(developerId.toUpperCase()));
    } else if (!gameToLookup) {
      throw new GameNotFoundException("Game Id: ".concat(game.getGameId().toUpperCase()));
    } else {
      game.setGameDeveloper(developerToLookup.get());
      gameRepository.saveAndFlush(game);
    }
  }

  @DeleteMapping("/developers/{developerId}")
  public void deleteDeveloper(@PathVariable String developerId) {
    boolean developerToLookup = developerRepository.existsById(developerId);
    if (!developerToLookup) {
      throw new DeveloperNotFoundException("Developer Id: ".concat(developerId.toUpperCase()));
    } else {
      developerRepository.deleteById(developerId);
    }
  }

  @DeleteMapping("/games/{gameId}")
  public void deleteGame(@PathVariable String gameId) {
    boolean gameToLookup = gameRepository.existsById(gameId);
    if (!gameToLookup) {
      throw new GameNotFoundException("Game Id: ".concat(gameId.toUpperCase()));
    } else {
      gameRepository.deleteById(gameId);
    }
  }

  @GetMapping("/developercount")
  public Long getDeveloperCount() {
    return developerRepository.count();
  }

  @GetMapping("/gamecount")
  public Long getGameCount() {
    return gameRepository.count();
  }
}
