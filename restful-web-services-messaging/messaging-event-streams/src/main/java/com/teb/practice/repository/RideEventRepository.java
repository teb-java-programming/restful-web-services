package com.teb.practice.repository;

import com.teb.practice.model.RideEventEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideEventRepository extends JpaRepository<RideEventEntity, String> {

    List<RideEventEntity> findByRideIdOrderByCreatedAtAsc(String rideId);
}
