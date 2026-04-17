package com.teb.practice.repository;

import com.teb.practice.entity.Library;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {}
