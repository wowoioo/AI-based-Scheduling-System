package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

public class Professor {
    @ExcelIgnore
    @ExcelProperty("Professor ID")
    private int professorId;

    @ExcelProperty("Professor Name")
    private String professorName;
//    private String unavailableDate;

    public Professor(int professorId, String professorName) {
        this.professorId = professorId;
        this.professorName = professorName;
    }

    public int getProfessorId() {
        return this.professorId;
    }

    public String getProfessorName() {
        return this.professorName;
    }
}
