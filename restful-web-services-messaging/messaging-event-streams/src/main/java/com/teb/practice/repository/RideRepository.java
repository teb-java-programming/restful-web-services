package com.teb.practice.repository;

import com.teb.practice.model.Ride;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, String> {}
