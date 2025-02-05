package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.*;
import com.github.rayinfinite.scheduler.repository.CourseRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ClassroomService classroomService;

    @Mock
    private GAService gaService;

    @InjectMocks
    private AppService appService;

    @Test
    void testUpload() throws Exception {
        // 创建一个有效的 Excel 文件内容
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            ExcelGenerator.createTestExcel()
        );

        // 执行测试
        String result = appService.upload(file);

        // 验证结果
        assertThat(result).isEqualTo("success");
    }

    @Test
    void testDetectionUpload() throws IOException {
        // 创建一个模拟的 Excel 文件
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "detection_test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                ExcelGenerator.createTestExcel()
        );

        // 创建测试数据
        Registration testRegistration = new Registration("cohort01", 30, "course01");
        // 设置 mock 行为
        List<Registration> registrations = new ArrayList<>();
        registrations.add(testRegistration);

        doNothing().when(gaService).updateRegistrations(any());

        // 执行测试
        String result = appService.detectionUpload(file);

        // 验证结果
        assertThat(result).isEqualTo("success");
        verify(gaService).updateRegistrations(any());
        verify(gaService).detection(any(), any());
    }

    @Test
    public void testDetection() {
        // 创建模拟的 OutputData 列表
        List<OutputData> dataList = new ArrayList<>();

        OutputData outputData1 = new OutputData();
        outputData1.setPracticeArea("PA1");
        outputData1.setCourseName("Course1");
        outputData1.setCourseCode("CC01");
        outputData1.setDuration(2);
        outputData1.setSoftware("S1");
        outputData1.setCohort("Cohort1");
        outputData1.setRun(1);

        String dateString = "2025-02-01T00:00:00.000Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        outputData1.setCourseDate(date);

        outputData1.setWeek("Saturday");
        outputData1.setClassroom("Room1");
        outputData1.setTeacher1("Teacher1");
        outputData1.setTeacher2("Teacher2");
        outputData1.setTeacher3("Teacher3");
        outputData1.setManager("Teacher1");
        outputData1.setCert("GradCert1");

        dataList.add(outputData1);

        // 模拟 classroomService 和 gaService 的返回值
        when(classroomService.getAllClassrooms()).thenReturn(new ArrayList<>());
        when(gaService.detection(eq(dataList), anyList())).thenReturn(dataList);

        // 调用检测方法
        appService.detection(dataList);

        // 验证 courseList 的大小，即数据保存是否成功
        verify(courseRepository, times(1)).deleteAll(); // 确保删除操作被调用一次
        verify(courseRepository, times(1)).saveAll(anyList()); // 确保保存操作被调用一次

        // 通过 saveAll 返回的 List 来验证数据大小
        ArgumentCaptor<List<Course>> captor = ArgumentCaptor.forClass(List.class);
        verify(courseRepository).saveAll(captor.capture());
        List<Course> savedCourseList = captor.getValue();

        // 验证数据保存数量
        assertEquals(1, savedCourseList.size(), "Expected 1 course to be saved.");
    }


    @Test
    void testGap() {
        // 准备测试数据
        List<Course> courseList = List.of(new Course());
        List<Cohort> cohortList = List.of(new Cohort());
        List<Timeslot> timeslotList = List.of(new Timeslot());
        List<Course> resultList = List.of(new Course());

        // 设置 mock 行为
        when(gaService.gap(any(), any(), any(), any())).thenReturn(resultList);

        // 执行测试
        appService.gap(courseList, cohortList, timeslotList);

        // 验证调用
        verify(courseRepository).deleteAll();
        verify(courseRepository).saveAll(resultList);
        verify(gaService).gap(eq(courseList), eq(cohortList), eq(timeslotList), any());
    }

    @Test
    void testFindByCourseDateBetween() throws Exception {
        // 准备测试数据
        List<Course> courseList = new ArrayList<>(Arrays.asList(
            createCourse("Teacher1", null, null),
            createCourse("Teacher2", "Teacher3", null)
        ));

        // 设置 mock 行为
        when(courseRepository.findByCourseDateBetween(any(), any())).thenReturn(courseList);

        // 执行测试 - 无教师过滤
        List<Course> result1 = appService.findByCourseDateBetween(
            "2024-03-20T00:00:00.000Z",
            "2024-03-21T00:00:00.000Z",
            null,
            null
        );
        assertThat(result1).hasSize(2);

        // 执行测试 - 有教师过滤
        List<Course> result2 = appService.findByCourseDateBetween(
                "2024-03-20T00:00:00.000Z",
                "2024-03-21T00:00:00.000Z",
            Arrays.asList("Teacher1"),
            null
        );
        assertThat(result2).hasSize(1);
    }

    @Test
    void testGetAllTeachers() {
        // 准备测试数据
        when(courseRepository.findTeacher1()).thenReturn(Arrays.asList("Teacher1", "Teacher2"));
        when(courseRepository.findTeacher2()).thenReturn(Arrays.asList("Teacher2", "Teacher3"));
        when(courseRepository.findTeacher3()).thenReturn(Arrays.asList("Teacher3", "Teacher4"));

        // 执行测试
        List<String> result = appService.getAllTeachers();

        // 验证结果
        assertThat(result)
            .hasSize(4)
            .containsExactly("Teacher1", "Teacher2", "Teacher3", "Teacher4");
    }
    @Test
    void testGetAllCohorts() {
        // 准备测试数据
        when(courseRepository.findCohort()).thenReturn(Arrays.asList("Cohort1", "Cohort2"));

        // 执行测试
        List<String> result = appService.getAllCohorts();

        // 验证结果
        assertThat(result).hasSize(2).containsExactly("Cohort1", "Cohort2");
    }


    private Course createCourse(String teacher1, String teacher2, String teacher3) {
        Course course = new Course();
        course.setTeacher1(teacher1);
        course.setTeacher2(teacher2);
        course.setTeacher3(teacher3);
        return course;
    }



    // 辅助方法：创建测试用的 Excel 文件
    private static class ExcelGenerator {
        public static byte[] createTestExcel() throws IOException {
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Sheet1");

                // 创建表头
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Course Code");
                headerRow.createCell(1).setCellValue("Course Name");
                headerRow.createCell(2).setCellValue("Duration");

                // 创建数据行
                Row dataRow = sheet.createRow(1);
                dataRow.createCell(0).setCellValue("CS101");
                dataRow.createCell(1).setCellValue("Intro to CS");
                dataRow.createCell(2).setCellValue(120);

                // 写入到字节数组
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    workbook.write(bos);
                    return bos.toByteArray();
                }
            }
        }
    }
} 