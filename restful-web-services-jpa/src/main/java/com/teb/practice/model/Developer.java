package com.teb.practice.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Developer {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private String developerId;

  @NotNull
  @Size(min = 10, max = 20, message = "Developer name should be between 10 to 20 characters.")
  private String developerName;

  @OneToMany(mappedBy = "gameDeveloper", orphanRemoval = true)
  private Set<Game> gameSet;
}
