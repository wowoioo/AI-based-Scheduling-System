package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Response;
import com.github.rayinfinite.scheduler.repository.ClassroomRepository;
import com.github.rayinfinite.scheduler.service.AppService;
import com.github.rayinfinite.scheduler.service.ClassroomService;
import com.github.rayinfinite.scheduler.utils.LoginUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AppController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AppControllerDiffblueTest {
    @Autowired
    private AppController appController;

    @MockitoBean
    private AppService appService;

    @MockitoBean
    private ClassroomService classroomService;

    @MockitoBean
    private LoginUtil loginUtil;

    /**
     * Test {@link AppController#getExcel(MultipartFile, OidcUser)} with
     * {@code file}, {@code principal}.
     * <ul>
     *   <li>Then return Data is {@code Upload}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppController#getExcel(MultipartFile, OidcUser)}
     */
    @Test
    @DisplayName("Test getExcel(MultipartFile, OidcUser) with 'file', 'principal'; then return Data is 'Upload'")
    void testGetExcelWithFilePrincipal_thenReturnDataIsUpload() throws IOException {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        AppService service = mock(AppService.class);
        when(service.upload(Mockito.any())).thenReturn("Upload");
        LoginUtil loginUtil = mock(LoginUtil.class);
        doNothing().when(loginUtil).log(Mockito.any(), Mockito.any(), Mockito.any());
        AppController appController = new AppController(service, new ClassroomService(mock(ClassroomRepository.class)),
                loginUtil);
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("foo.txt");

        // Act
        Response actualExcel = appController.getExcel(file, null);

        // Assert
        verify(service).upload(isA(MultipartFile.class));
        verify(loginUtil).log(eq("POST /upload"), eq("foo.txt"), isNull());
        verify(file).getOriginalFilename();
        assertEquals("Upload", actualExcel.getData());
        assertNull(actualExcel.getCode());
        assertNull(actualExcel.getMessage());
    }

    /**
     * Test {@link AppController#getExcel(MultipartFile)} with {@code file}.
     * <ul>
     *   <li>Given {@link AppService} {@link AppService#upload(MultipartFile)} return
     * {@code Upload}.</li>
     *   <li>Then return Data is {@code Upload}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppController#getExcel(MultipartFile)}
     */
    @Test
    @DisplayName("Test getExcel(MultipartFile) with 'file'; given AppService upload(MultipartFile) return 'Upload'; "
            + "then return Data is 'Upload'")
    void testGetExcelWithFile_givenAppServiceUploadReturnUpload_thenReturnDataIsUpload() throws IOException {
        // Arrange
        when(appService.upload(Mockito.any())).thenReturn("Upload");

        // Act
        Response actualExcel = appController
                .getExcel(new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));

        // Assert
        verify(appService).upload(isA(MultipartFile.class));
        assertEquals("Upload", actualExcel.getData());
        assertNull(actualExcel.getCode());
        assertNull(actualExcel.getMessage());
    }

    /**
     * Test {@link AppController#getExcel(MultipartFile)} with {@code file}.
     * <ul>
     *   <li>Then throw {@link IOException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppController#getExcel(MultipartFile)}
     */
    @Test
    @DisplayName("Test getExcel(MultipartFile) with 'file'; then throw IOException")
    void testGetExcelWithFile_thenThrowIOException() throws IOException {
        // Arrange
        when(appService.upload(Mockito.any())).thenThrow(new IOException("foo"));

        // Act and Assert
        assertThrows(IOException.class, () -> appController
                .getExcel(new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")))));
        verify(appService).upload(isA(MultipartFile.class));
    }

    /**
     * Test {@link AppController#getData(String, String, List, List)}.
     * <p>
     * Method under test: {@link AppController#getData(String, String, List, List)}
     */
    @Test
    @DisplayName("Test getData(String, String, List, List)")
    void testGetData2() throws Exception {
        // Arrange
        when(appService.findByCourseDateBetween(Mockito.any(), Mockito.any(),
                Mockito.any(),
                Mockito.any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/data")
                .param("endStr", "foo")
                .param("startStr", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(appController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link AppController#getAllTeachers()}.
     * <p>
     * Method under test: {@link AppController#getAllTeachers()}
     */
    @Test
    @DisplayName("Test getAllTeachers()")
    void testGetAllTeachers() throws Exception {
        // Arrange
        when(appService.getAllTeachers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/teacher");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(appController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link AppController#getData(String, String, List)}.
     * <p>
     * Method under test: {@link AppController#getData(String, String, List)}
     */
    @Test
    @DisplayName("Test getData(String, String, List)")
    void testGetData() throws Exception {
        // Arrange
        when(appService.findByCourseDateBetween(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/data")
                .param("endStr", "foo")
                .param("startStr", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(appController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link AppController#getAllCohorts()}.
     * <p>
     * Method under test: {@link AppController#getAllCohorts()}
     */
    @Test
    @DisplayName("Test getAllCohorts()")
    void testGetAllCohorts() throws Exception {
        // Arrange
        when(appService.getAllCohorts()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/student");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(appController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link AppController#getAllClassrooms()}.
     * <ul>
     *   <li>Given {@code /classname}.</li>
     *   <li>When formLogin.</li>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppController#getAllClassrooms()}
     */
    @Test
    @DisplayName("Test getAllClassrooms(); given '/classname'; when formLogin; then status isNotFound()")
    void testGetAllClassrooms_givenClassname_whenFormLogin_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(classroomService.getAllClassroomNames()).thenReturn(new ArrayList<>());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder =
                SecurityMockMvcRequestBuilders.formLogin();

        // Act
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(appController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link AppController#getAllClassrooms()}.
     * <ul>
     *   <li>When {@link MockMvcRequestBuilders#get(String, Object[])}
     * {@code /classname}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppController#getAllClassrooms()}
     */
    @Test
    @DisplayName("Test getAllClassrooms(); when get(String, Object[]) '/classname'; then status isOk()")
    void testGetAllClassrooms_whenGetClassname_thenStatusIsOk() throws Exception {
        // Arrange
        when(classroomService.getAllClassroomNames()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/classname");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(appController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }
}
