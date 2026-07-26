package com.example.medicalsuppliesmanagement.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supplies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String provider;

    private String unitOfCalculation;

    @Builder.Default
    private Integer quantity = 0;

    private String specification;

    @Builder.Default
    private Boolean isDeleted = false;
}
