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
public class ClashData {
    @ColumnWidth(22)
    @ExcelProperty("Clash Type")
    private String clashType;
    @ColumnWidth(17)
    @ExcelProperty("Clash Num")
    private Integer clashCount;
    @ColumnWidth(17)
    @ExcelProperty("Room")
    private String roomName;
    @ColumnWidth(22)
    @ExcelProperty("Date")
    private String date;

//    public ClashData(String clashType, Integer size, String roomName, String date) {
//        this.clashType = clashType;
//        this.clashCount = size;
//        this.roomName = roomName;
//        this.date = date;
//    }
}
