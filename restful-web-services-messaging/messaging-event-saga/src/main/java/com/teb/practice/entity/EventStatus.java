package com.teb.practice.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import static org.hibernate.type.SqlTypes.JSON;

import static java.time.LocalDateTime.now;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "event_status")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventStatus {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String sagaId;
    private String eventType;
    @Setter private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JdbcTypeCode(JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @PrePersist
    public void prePersist() {

        LocalDateTime now = now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = now();
    }
}
