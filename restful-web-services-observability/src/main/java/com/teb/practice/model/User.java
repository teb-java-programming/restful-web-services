package com.teb.practice.model;

import static java.util.UUID.randomUUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class User {

    @Builder.Default String id = randomUUID().toString();
    @NonNull String name;
    @NonNull Integer age;
}
