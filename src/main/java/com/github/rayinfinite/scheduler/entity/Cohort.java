package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cohort {
    @Id
    @ExcelIgnore
    @ExcelProperty("Cohort ID")
    private int cohortId;

    @ExcelProperty("Cohort Size")
    private int cohortSize;

    @ExcelProperty("Cohort Type")
    private String cohortType;

    @ExcelProperty("Cohort")
    private String cohort;

    @ExcelIgnore
    private int typeId;
    @ExcelIgnore
    private int[] courseIds;

    public Cohort(int cohortId, String cohort, int cohortSize, int typeId, String cohortType) {
        this.cohortId = cohortId;
        this.cohort = cohort;
        this.cohortSize = cohortSize;
        this.typeId = typeId;
        this.cohortType = cohortType;
    }
}
