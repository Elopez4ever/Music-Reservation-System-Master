package com.bookingsystem.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Building {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}