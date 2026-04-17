package com.teb.practice.repository;

import com.teb.practice.entity.Developer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {}
