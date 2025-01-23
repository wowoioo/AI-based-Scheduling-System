package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.exception.ExcelDataConvertException;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BaseExcelReader测试")
class BaseExcelReaderTest {
    private BaseExcelReader<TestData> reader;
    private static final String TEST_FILE = "inputdata2.xlsx";
    private static final int EXPECTED_ROW_COUNT = 20; // inputdata2.xlsx 的实际行数

    @BeforeEach
    void setUp() {
        reader = new BaseExcelReader<>();
    }

    @Nested
    @DisplayName("基本功能测试")
    class BasicFunctionalityTests {
        @Test
        @DisplayName("测试正常读取Excel文件")
        void testNormalReading() throws IOException {
            // 准备测试
            File excelFile = getTestFile();

            // 执行测试
            EasyExcel.read(excelFile, TestData.class, reader).sheet().doRead();
            List<TestData> dataList = reader.getDataList();

            // 验证结果
            assertNotNull(dataList, "数据列表不应为空");
            assertFalse(dataList.isEmpty(), "数据列表不应为空");
            assertEquals(EXPECTED_ROW_COUNT, dataList.size(),
                    String.format("预期行数 %d, 实际行数 %d", EXPECTED_ROW_COUNT, dataList.size()));

            // 验证第一行数据
            TestData firstRow = dataList.get(0);
            assertNotNull(firstRow.getPracticeArea(), "Practice Area 不应为空");
            assertNotNull(firstRow.getCourseName(), "Course Name 不应为空");
        }

        @Test
        @DisplayName("测试文件不存在异常")
        void testFileNotFound() {
            File nonExistentFile = new File("nonexistent.xlsx");

            assertThrows(IOException.class, () ->
                            EasyExcel.read(nonExistentFile, TestData.class, reader).sheet().doRead(),
                    "读取不存在的文件应抛出IOException"
            );
        }
    }

    @Nested
    @DisplayName("异常处理测试")
    class ExceptionHandlingTests {
        @Test
        @DisplayName("测试数据转换异常")
        void testDataConversionException() {
            // 创建一个包含无效数据的文件路径
            String invalidFilePath = "test_invalid_data.xlsx";
            URL resource = getClass().getClassLoader().getResource(invalidFilePath);

            // 如果测试文件存在，则执行测试
            if (resource != null) {
                File invalidFile = new File(resource.getFile());
                assertThrows(ExcelDataConvertException.class, () ->
                                EasyExcel.read(invalidFile, TestData.class, reader).sheet().doRead(),
                        "无效数据应该触发转换异常"
                );
            }
        }
    }

    // 测试用的数据类，根据实际的Excel文件结构定义
    @Data
    public static class TestData {
        @ExcelProperty("Practice Area")
        private String practiceArea;

        @ExcelProperty("Course Name")
        private String courseName;

        @ExcelProperty("Course Code")
        private String courseCode;

        @ExcelProperty("Duration")
        private Integer duration;

        @ExcelProperty("Cohort ID")
        private Integer cohortId;

        // ... 其他字段根据实际Excel列定义
    }

    // 工具方法
    private File getTestFile() throws IOException {
        URL resource = getClass().getClassLoader().getResource(TEST_FILE);
        assertNotNull(resource, "测试文件不存在: " + TEST_FILE);
        return new File(resource.getFile());
    }
}
