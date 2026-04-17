package com.teb.practice.service;

import com.teb.practice.dto.request.DeveloperRequest;
import com.teb.practice.dto.response.DeveloperResponse;
import com.teb.practice.dto.response.GameResponse;
import com.teb.practice.entity.Developer;
import com.teb.practice.exception.ResourceNotFoundException;
import com.teb.practice.repository.DeveloperRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    public List<DeveloperResponse> getDevelopers() {

        return developerRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public DeveloperResponse getDeveloper(Long id) {

        return mapToResponse(findDeveloper(id));
    }

    public DeveloperResponse createDeveloper(DeveloperRequest request) {

        return mapToResponse(
                developerRepository.save(
                        Developer.builder().name(request.name()).email(request.email()).build()));
    }

    public DeveloperResponse updateDeveloper(Long id, DeveloperRequest request) {

        Developer developer = findDeveloper(id);

        developer.setName(request.name());
        developer.setEmail(request.email());

        return mapToResponse(developerRepository.save(developer));
    }

    public DeveloperResponse patchDeveloper(Long id, DeveloperRequest request) {

        Developer developer = findDeveloper(id);

        if (request.name() != null) developer.setName(request.name());
        if (request.email() != null) developer.setEmail(request.email());

        return mapToResponse(developerRepository.save(developer));
    }

    public void deleteDeveloper(Long id) {

        developerRepository.delete(findDeveloper(id));
    }

    private DeveloperResponse mapToResponse(Developer developer) {

        return DeveloperResponse.builder()
                .id(developer.getId())
                .name(developer.getName())
                .email(developer.getEmail())
                .games(getListOfGames(developer))
                .build();
    }

    private Developer findDeveloper(Long id) {

        return developerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));
    }

    private List<GameResponse> getListOfGames(Developer developer) {

        return developer.getGames().stream()
                .map(
                        game ->
                                GameResponse.builder()
                                        .id(game.getId())
                                        .title(game.getTitle())
                                        .genre(game.getGenre())
                                        .developerId(developer.getId())
                                        .build())
                .toList();
    }
}
