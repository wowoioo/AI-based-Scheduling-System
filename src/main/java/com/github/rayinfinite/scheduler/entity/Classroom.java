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
    private Integer id;

    private String name;
    private Integer size;

    public Classroom(int roomId, String roomNumber, int capacity) {
        this.id = roomId;
        this.name = roomNumber;
        this.size = capacity;
    }

    public Classroom() {

    }


    public int getRoomId() {
        return this.id;
    }

    public String getRoomNumber() {
        return this.name;
    }

    public int getRoomCapacity() {
        return this.size;
    }
}