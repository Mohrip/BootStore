package com.io.CoreBackend.authors.entity;

import jakarta.persistence.*;
import lombok.*;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "authors")
@Builder
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


}
