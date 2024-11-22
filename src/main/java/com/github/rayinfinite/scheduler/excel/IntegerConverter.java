package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;

public class IntegerConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) {
        String value = context.getReadCellData().getStringValue();
        if (value == null || value.isBlank() || value.equals("-")) {
            return null;
        }
        return Integer.valueOf(value);
    }
}
