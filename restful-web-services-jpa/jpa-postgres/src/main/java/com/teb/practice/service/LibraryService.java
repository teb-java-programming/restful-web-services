package com.teb.practice.service;

import com.teb.practice.dto.request.LibraryRequest;
import com.teb.practice.dto.response.LibraryResponse;
import com.teb.practice.entity.Library;
import com.teb.practice.exception.ResourceNotFoundException;
import com.teb.practice.repository.LibraryRepository;
import com.teb.practice.repository.StockRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final StockRepository stockRepository;

    public List<LibraryResponse> getLibraries() {

        return libraryRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public LibraryResponse getLibrary(Long id) {

        return mapToResponse(findLibrary(id));
    }

    public LibraryResponse createLibrary(LibraryRequest request) {

        return mapToResponse(
                libraryRepository.save(
                        Library.builder()
                                .name(request.name())
                                .location(request.location())
                                .build()));
    }

    public LibraryResponse updateLibrary(Long id, LibraryRequest request) {

        Library library = findLibrary(id);

        library.setName(request.name());
        library.setLocation(request.location());

        return mapToResponse(libraryRepository.save(library));
    }

    public void deleteLibrary(Long id) {

        libraryRepository.delete(findLibrary(id));
    }

    private LibraryResponse mapToResponse(Library library) {

        return LibraryResponse.builder()
                .id(library.getId())
                .name(library.getName())
                .location(library.getLocation())
                .stock(stockRepository.getTotalStockByLibraryId(library.getId()))
                .build();
    }

    private Library findLibrary(Long id) {

        return libraryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found"));
    }
}
