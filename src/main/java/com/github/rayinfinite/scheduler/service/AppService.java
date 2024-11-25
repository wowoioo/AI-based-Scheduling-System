package com.github.rayinfinite.scheduler.service;

import com.alibaba.excel.EasyExcel;
import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.excel.ExcelReader;
import com.github.rayinfinite.scheduler.repository.InputDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {
    private final InputDataRepository inputDataRepository;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    public String upload(MultipartFile file) throws IOException {
        log.info("Uploading file: {}", file.getOriginalFilename());
        ExcelReader excelReader = new ExcelReader(inputDataRepository);
        EasyExcel.read(file.getInputStream(), InputData.class, excelReader).sheet().doRead();
        return "success";
    }

    public List<InputData> findByCourseDateBetween(String startDate, String endDate, List<String> teachers) throws ParseException {
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        List<InputData> data = inputDataRepository.findByCourseDateBetween(start, end);
        if (teachers != null && !teachers.isEmpty()) {
            data.removeIf(inputData -> !teachers.contains(inputData.getTeacher1())
                    && !teachers.contains(inputData.getTeacher2())
                    && !teachers.contains(inputData.getTeacher3()));
        }
        return data;
    }

    public List<String> getAllTeachers() {
        List<String> list = new ArrayList<>();
        list.addAll(inputDataRepository.findTeacher1());
        list.addAll(inputDataRepository.findTeacher2());
        list.addAll(inputDataRepository.findTeacher3());
        return list.stream().filter(Objects::nonNull).filter(s -> !s.isEmpty())
                .distinct().sorted().toList();
    }

}
