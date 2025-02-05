package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ColumnWidth(16)
@HeadFontStyle(fontName = "Arial", fontHeightInPoints = 11, bold = BooleanEnum.TRUE)
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 15)
public class Registration {
    @ColumnWidth(17)
    @ExcelProperty("Cohort")
    private String cohort;
    @ColumnWidth(17)
    @ExcelProperty("Headcount")
    private int headcount;
    @ColumnWidth(17)
    @ExcelProperty("Course Name")
    private String courseName;

}
