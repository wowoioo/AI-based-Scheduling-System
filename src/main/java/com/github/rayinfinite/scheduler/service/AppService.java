package com.github.rayinfinite.scheduler.service;

import com.alibaba.excel.EasyExcel;
import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.excel.ExcelReader;
import com.github.rayinfinite.scheduler.repository.InputDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {
    private final InputDataRepository inputDataRepository;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    public String upload(MultipartFile file) throws IOException {
        System.out.println("Uploading file: " + file.getOriginalFilename());
        ExcelReader excelReader = new ExcelReader(inputDataRepository);
        EasyExcel.read(file.getInputStream(), InputData.class, excelReader).sheet().doRead();
        return "success";
    }

    public List<InputData> findByCourseDateBetween(String startDate, String endDate) throws ParseException {
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        return inputDataRepository.findByCourseDateBetween(start, end);
    }
}
