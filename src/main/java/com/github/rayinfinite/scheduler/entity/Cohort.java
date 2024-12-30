package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
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
    private int cohortId;

    private int cohortSize;
    private String cohortType;
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
