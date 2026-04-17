package com.teb.practice.service;

import com.teb.practice.dto.request.StockRequest;
import com.teb.practice.dto.response.StockResponse;
import com.teb.practice.entity.Book;
import com.teb.practice.entity.Library;
import com.teb.practice.entity.Stock;
import com.teb.practice.exception.ResourceNotFoundException;
import com.teb.practice.repository.BookRepository;
import com.teb.practice.repository.LibraryRepository;
import com.teb.practice.repository.StockRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;

    public List<StockResponse> getStocks() {

        return stockRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public StockResponse getStock(Long id) {

        return mapToResponse(findStock(id));
    }

    public StockResponse createStock(StockRequest request) {

        return mapToResponse(
                stockRepository.save(
                        Stock.builder()
                                .book(findBook(request.bookId()))
                                .library(findLibrary(request.libraryId()))
                                .quantity(request.quantity())
                                .build()));
    }

    public StockResponse updateStock(Long id, StockRequest request) {

        Stock stock = findStock(id);

        stock.setBook(findBook(request.bookId()));
        stock.setLibrary(findLibrary(request.libraryId()));
        stock.setQuantity(request.quantity());

        return mapToResponse(stockRepository.save(stock));
    }

    public void deleteStock(Long id) {

        stockRepository.delete(findStock(id));
    }

    private StockResponse mapToResponse(Stock stock) {

        return StockResponse.builder()
                .id(stock.getId())
                .bookTitle(stock.getBook().getTitle())
                .libraryName(stock.getLibrary().getName())
                .quantity(stock.getQuantity())
                .build();
    }

    private Stock findStock(Long id) {

        return stockRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
    }

    private Book findBook(Long id) {

        return bookRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    private Library findLibrary(Long id) {

        return libraryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found"));
    }
}
