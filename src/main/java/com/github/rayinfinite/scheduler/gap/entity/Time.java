package com.github.rayinfinite.scheduler.gap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Time implements Serializable {
    private int weekOrder;    // 星期几
    private int courseOrder;  // 课程顺序（一天中的节次）
}
