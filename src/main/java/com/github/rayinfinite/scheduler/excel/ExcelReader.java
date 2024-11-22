package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.repository.InputDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class ExcelReader implements ReadListener<InputData> {
    private static final int BATCH_COUNT = 1000;
    private final List<InputData> cachedDataList = new ArrayList<>(1100);
    private final InputDataRepository repository;

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        repository.deleteAll();
        log.info("Deleted all records");
    }

    @Override
    public void invoke(InputData data, AnalysisContext context) {
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    private void saveData() {
        log.info("Saved {} records", cachedDataList.size());
        repository.saveAll(cachedDataList);
        log.info("Saved {} records", cachedDataList.size());
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("An exception occurred while reading the data: {}", exception.getMessage());
        log.error("Line {}", context.readRowHolder().getRowIndex());
        throw exception;
    }
}
