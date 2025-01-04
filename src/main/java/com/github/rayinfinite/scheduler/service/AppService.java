package com.github.rayinfinite.scheduler.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.entity.Cohort;
import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.entity.Timeslot;
import com.github.rayinfinite.scheduler.excel.BaseExcelReader;
import com.github.rayinfinite.scheduler.repository.ClassroomRepository;
import com.github.rayinfinite.scheduler.repository.InputDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {
    private final InputDataRepository inputDataRepository;
    private final ClassroomRepository classroomRepository;
    private final GAService gaService;
    private final Lock lock = new ReentrantLock();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    public String upload(MultipartFile file) throws IOException {
        lock.lock();
        log.info("Uploading file: {}", file.getOriginalFilename());
        BaseExcelReader<InputData> courseReader = new BaseExcelReader<>();
        BaseExcelReader<Cohort> cohortReader = new BaseExcelReader<>();
        BaseExcelReader<Timeslot> timeReader = new BaseExcelReader<>();
        try (ExcelReader excelReader = EasyExcel.read(file.getInputStream()).build()) {
            ReadSheet courseSheet =
                    EasyExcel.readSheet(0).head(InputData.class).registerReadListener(courseReader).build();
            ReadSheet cohortSheet =
                    EasyExcel.readSheet(1).head(Cohort.class).registerReadListener(cohortReader).build();
            ReadSheet timeSheet = EasyExcel.readSheet(2).head(Timeslot.class).registerReadListener(timeReader).build();
            excelReader.read(courseSheet, cohortSheet, timeSheet);
        }
        lock.unlock();

        Thread.ofVirtual().start(() -> {
            jgap(courseReader.getDataList(), cohortReader.getDataList(), timeReader.getDataList());
        });
        return "success";
    }

    public void jgap(List<InputData> inputDataList, List<Cohort> cohortList, List<Timeslot> timeslotList) {
        var result = gaService.jgap(inputDataList, cohortList, timeslotList, getAllClassrooms());
        for (InputData data : result) {
            log.info(data.toString());
        }
        inputDataRepository.deleteAll();
        inputDataRepository.saveAll(result);
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

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public Classroom updateClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public Classroom deleteClassroom(Long id) {
        Classroom classroom = classroomRepository.findById(id).orElse(null);
        if (classroom != null) {
            classroomRepository.delete(classroom);
        }
        return classroom;
    }
}
