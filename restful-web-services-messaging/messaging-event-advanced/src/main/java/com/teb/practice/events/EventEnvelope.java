//package com.teb.practice.events;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class EventEnvelope<T> {
//
//    private String eventType;
//    private String version; // e.g. "1.0"
//    private LocalDateTime timestamp; // ISO via Jackson JavaTimeModule
//    private String key;
//    private T payload;
//}
