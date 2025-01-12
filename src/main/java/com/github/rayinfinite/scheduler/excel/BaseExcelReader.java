package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class BaseExcelReader<T> implements ReadListener<T> {
    @Getter
    private final List<T> dataList = new ArrayList<>(1100);

    @Override
    public void invoke(T data, AnalysisContext context) {
        dataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("An exception occurred while reading the data: {}", exception.getMessage());
        log.error("Line {}", context.readRowHolder().getRowIndex());
        throw exception;
    }
}
