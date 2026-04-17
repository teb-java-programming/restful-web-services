package com.teb.practice.repository;

import com.teb.practice.entity.Stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("select coalesce(sum(s.quantity), 0) from Stock s where s.library.id = :libraryId")
    Long getTotalStockByLibraryId(Long libraryId);
}
