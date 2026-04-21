package com.teb.practice.model;

import static java.util.UUID.randomUUID;

import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(name = "User", description = "A system user")
public class User {

    @Builder.Default
    @Schema(description = "Unique identifier of the user")
    String id = randomUUID().toString();

    @NonNull
    @Schema(description = "Name of the user")
    String name;

    @NonNull
    @Schema(description = "Age of the user")
    Integer age;
}
