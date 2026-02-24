package com.teb.practice.model;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private String gameId;

  @NotNull
  @Size(min = 10, max = 40, message = "Game name should be between 10 to 40 characters.")
  private String gameName;

  @ManyToOne(fetch = LAZY)
  @JsonIgnore
  private Developer gameDeveloper;
}
