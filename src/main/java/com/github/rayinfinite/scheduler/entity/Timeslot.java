package com.github.rayinfinite.scheduler.entity;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.github.rayinfinite.scheduler.excel.DateConverter;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Timeslot {
    @Id
    @ExcelIgnore
    private int timeslotId;
    @ExcelProperty(converter = DateConverter.class)
    private Date timeslot;
}
