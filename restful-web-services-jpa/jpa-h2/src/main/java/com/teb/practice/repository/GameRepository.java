package com.teb.practice.repository;

import com.teb.practice.entity.Game;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {}
