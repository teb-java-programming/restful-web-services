package com.teb.practice.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.teb.practice.dto.request.StockRequest;
import com.teb.practice.dto.response.StockResponse;
import com.teb.practice.response.ApiResponse;
import com.teb.practice.service.StockService;

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
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ApiResponse<List<StockResponse>> getStocks() {

        return new ApiResponse<>("Stock list", stockService.getStocks());
    }

    @GetMapping("/{id}")
    public ApiResponse<StockResponse> getStock(@PathVariable Long id) {

        return new ApiResponse<>("Stock found", stockService.getStock(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StockResponse>> createStock(
            @Valid @RequestBody StockRequest request) {

        return status(201)
                .body(new ApiResponse<>("Stock created", stockService.createStock(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StockResponse>> updateStock(
            @PathVariable Long id, @RequestBody StockRequest request) {

        return ok(new ApiResponse<>("Stock updated", stockService.updateStock(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStock(@PathVariable Long id) {

        stockService.deleteStock(id);

        return ok(new ApiResponse<>("Stock deleted", null));
    }
}
