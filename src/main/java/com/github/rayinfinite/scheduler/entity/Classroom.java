package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
    @ExcelIgnore
    @ExcelProperty("ID")
    private Integer id;

    @ExcelProperty("Name")
    private String name;

    @ExcelProperty("Size")
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