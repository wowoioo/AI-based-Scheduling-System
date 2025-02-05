package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@ColumnWidth(16)
@HeadFontStyle(fontName = "Arial", fontHeightInPoints = 11, bold = BooleanEnum.TRUE)
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 15)
public class RoomUtilization {
    @ColumnWidth(17)
    @ExcelProperty("Room")
    private String room;
    @ColumnWidth(17)
    @ExcelProperty("Used Days")
    private int usedDays;
    @ColumnWidth(17)
    @ExcelProperty("Utilization Rate")
    private String utilizationRate;

    public RoomUtilization(String room, int usedDays, String utilizationRate) {
        this.room = room;
        this.usedDays = usedDays;
        this.utilizationRate = utilizationRate;
    }
}
