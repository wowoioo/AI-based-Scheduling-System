package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter<Date> {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d");

    @Override
    public Class<?> supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public Date convertToJavaData(ReadConverterContext<?> context) {
        String value = context.getReadCellData().getStringValue();
        System.out.println(value);
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            System.out.println(formatter.parse(value));
            return formatter.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
}