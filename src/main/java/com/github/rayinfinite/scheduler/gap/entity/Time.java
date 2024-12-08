package com.github.rayinfinite.scheduler.gap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class Time implements Serializable {
    private int weekOrder;
    private int duration;
    private List<Date> dates;
//    private int courseOrder;
}
