package com.github.rayinfinite.scheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@Data
@Accessors(chain = true)
public class Classroom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer classSize;
    private String software;
}