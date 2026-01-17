package com.demo1.backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue
    Long id;

    @NotNull
    @NotBlank
    @Schema(name = "title", example = "Complete SpringBoot")
    String title;

    @Column(nullable = false)  // Ensure it's not null; defaults to false if not set
    Boolean isCompleted = false;  // Add default to avoid null issues

    @ManyToOne(fetch = FetchType.LAZY)  // Lazy load for performance
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key to users table
    private User user;
}