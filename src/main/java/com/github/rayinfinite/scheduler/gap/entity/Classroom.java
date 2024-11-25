package com.github.rayinfinite.scheduler.gap.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Classroom implements Serializable {
    private String id;        // 教室编号
    private int capacity;     // 教室容量
    private String type;      // 教室类型
}