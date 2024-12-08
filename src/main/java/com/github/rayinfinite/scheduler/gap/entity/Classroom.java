package com.github.rayinfinite.scheduler.gap.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Classroom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelIgnore
    private java.lang.String id;

    private java.lang.String Name;
    private java.lang.Integer Size;
    private String Software;
}