package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityExcelIntegrationTest<InputData> {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private BaseExcelReader<InputData> reader;
    private static final String TEST_FILE = "inputdata2.xlsx";
    private static final int EXPECTED_ROW_COUNT = 20;
    
    // 测试数据常量
    private static final String EXPECTED_PRACTICE_AREA = "PA2";
    private static final String EXPECTED_COURSE_NAME = "Course1";
    private static final String EXPECTED_COURSE_CODE = "CC2";
    
    @BeforeEach
    void setUp() {
        reader = new BaseExcelReader<>();
    }
    
    @Test
    void testReadExcelAndMapToEntity() throws IOException, ParseException {
        // 准备测试数据
        File excelFile = getTestFile();
        
        // 执行测试
        EasyExcel.read(excelFile, InputData.class, reader).sheet().doRead();
        List<InputData> dataList = reader.getDataList();
        
        // 验证基本数据
        assertBasicDataValidation(dataList);
        
        // 验证第一行数据
        validateFirstRow(dataList.get(0));
    }
    
    @Test
    void testReadEmptyExcel() {
        // 测试读取空文件的情况
        assertThrows(IllegalArgumentException.class, () -> {
            EasyExcel.read(new File("nonexistent.xlsx"), InputData.class, reader).sheet().doRead();
        });
    }
    
    private File getTestFile() throws IOException {
        URL resource = getClass().getClassLoader().getResource(TEST_FILE);
        assertNotNull(resource, "测试文件不存在: " + TEST_FILE);
        return new File(resource.getFile());
    }
    
    private void assertBasicDataValidation(List<InputData> dataList) {
        assertNotNull(dataList, "数据列表不应为空");
        assertEquals(EXPECTED_ROW_COUNT, dataList.size(), 
            String.format("预期行数 %d, 实际行数 %d", EXPECTED_ROW_COUNT, dataList.size()));
    }
    
    private void validateFirstRow(InputData data) throws ParseException {
        assertAll("首行数据验证",
            () -> assertEquals(EXPECTED_PRACTICE_AREA, data.getPracticeArea(), "Practice Area 不匹配"),
            () -> assertEquals(EXPECTED_COURSE_NAME, data.getCourseName(), "Course Name 不匹配"),
            () -> assertEquals(EXPECTED_COURSE_CODE, data.getCourseCode(), "Course Code 不匹配"),
            () -> assertEquals(1, data.getDuration(), "Duration 不匹配"),
            () -> assertEquals(1, data.getCohortId(), "Cohort ID 不匹配"),
            () -> assertEquals("Cohort1", data.getCohort(), "Cohort 不匹配"),
            () -> assertEquals(1, data.getRun(), "Run 不匹配"),
            () -> assertEquals("Lecturer9", data.getTeaching1(), "Teaching 1 不匹配"),
            () -> assertEquals("Lecturer1", data.getManager(), "Manager 不匹配"),
            () -> assertEquals("Grad Cert 1", data.getCert(), "Cert 不匹配"),
            () -> assertEquals(1, data.getProfessorNum(), "Professor Num 不匹配"),
            () -> assertEquals(30, data.getCohortSize(), "Cohort Size 不匹配"),
            () -> assertEquals(1, data.getCohortType(), "Cohort Type 不匹配"),
            () -> assertEquals("Cohort1", data.getCohort2(), "Cohort2 不匹配"),
            () -> assertEquals(formatter.parse("2024-02-07"), data.getCourseDate(), "Course Date 不匹配")
        );
    }
    
    // ... 其余代码保持不变 ...
}




