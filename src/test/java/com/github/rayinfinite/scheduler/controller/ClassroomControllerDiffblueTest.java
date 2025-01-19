package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.entity.Response;
import com.github.rayinfinite.scheduler.repository.ClassroomRepository;
import com.github.rayinfinite.scheduler.service.ClassroomService;
import com.github.rayinfinite.scheduler.utils.LoginUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ClassroomController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ClassroomControllerDiffblueTest {
    @Autowired
    private ClassroomController classroomController;

    @MockitoBean
    private ClassroomService classroomService;

    @MockitoBean
    private LoginUtil loginUtil;

    /**
     * Test {@link ClassroomController#getAllClassrooms()}.
     * <p>
     * Method under test: {@link ClassroomController#getAllClassrooms()}
     */
    @Test
    @DisplayName("Test getAllClassrooms()")
    void testGetAllClassrooms() throws Exception {
        // Arrange
        ArrayList<Classroom> classroomList = new ArrayList<>();
        classroomList.add(new Classroom());
        when(classroomService.getAllClassrooms()).thenReturn(classroomList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/classroom");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(classroomController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"code\":\"success\",\"message\":\"success\",\"data\":[{\"id\":null,\"name\":null,"
                                + "\"size\":null,\"software\":null}]}"));
    }

    /**
     * Test {@link ClassroomController#getAllClassrooms()}.
     * <ul>
     *   <li>Given {@code /classroom}.</li>
     *   <li>When formLogin.</li>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ClassroomController#getAllClassrooms()}
     */
    @Test
    @DisplayName("Test getAllClassrooms(); given '/classroom'; when formLogin; then status isNotFound()")
    void testGetAllClassrooms_givenClassroom_whenFormLogin_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(classroomService.getAllClassrooms()).thenReturn(new ArrayList<>());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder =
                SecurityMockMvcRequestBuilders.formLogin();

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(classroomController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link ClassroomController#getAllClassrooms()}.
     * <ul>
     *   <li>Then content string
     * {@code {"code":"success","message":"success","data":[]}}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ClassroomController#getAllClassrooms()}
     */
    @Test
    @DisplayName("Test getAllClassrooms(); then content string '{\"code\":\"success\",\"message\":\"success\","
            + "\"data\":[]}'")
    void testGetAllClassrooms_thenContentStringCodeSuccessMessageSuccessData() throws Exception {
        // Arrange
        when(classroomService.getAllClassrooms()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/classroom");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(classroomController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link ClassroomController#updateClassroom(Classroom)} with
     * {@code classroom}.
     * <p>
     * Method under test: {@link ClassroomController#updateClassroom(Classroom)}
     */
    @Test
    @DisplayName("Test updateClassroom(Classroom) with 'classroom'")
    void testUpdateClassroomWithClassroom() {
        // Arrange
        Classroom classroom = new Classroom();
        when(classroomService.updateClassroom(Mockito.any())).thenReturn(classroom);

        // Act
        Response actualUpdateClassroomResult = classroomController.updateClassroom(new Classroom());

        // Assert
        verify(classroomService).updateClassroom(isA(Classroom.class));
        assertEquals("success", actualUpdateClassroomResult.getCode());
        assertEquals("success", actualUpdateClassroomResult.getMessage());
        assertSame(classroom, actualUpdateClassroomResult.getData());
    }

    /**
     * Test {@link ClassroomController#updateClassroom(Classroom, OidcUser)} with
     * {@code classroom}, {@code principal}.
     * <ul>
     *   <li>Then calls {@link CrudRepository#save(Object)}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ClassroomController#updateClassroom(Classroom, OidcUser)}
     */
    @Test
    @DisplayName("Test updateClassroom(Classroom, OidcUser) with 'classroom', 'principal'; then calls save(Object)")
    void testUpdateClassroomWithClassroomPrincipal_thenCallsSave() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        ClassroomRepository classroomRepository = mock(ClassroomRepository.class);
        Classroom classroom = new Classroom();
        when(classroomRepository.save(Mockito.any())).thenReturn(classroom);
        ClassroomService service = new ClassroomService(classroomRepository);
        LoginUtil loginUtil = mock(LoginUtil.class);
        doNothing().when(loginUtil).log(Mockito.any(), Mockito.any(), Mockito.any());
        ClassroomController classroomController = new ClassroomController(service, loginUtil);

        // Act
        Response actualUpdateClassroomResult = classroomController.updateClassroom(new Classroom(), null);

        // Assert
        verify(loginUtil).log(eq("POST /classroom"), eq("Classroom(id=null, name=null, size=null, software=null)"),
                isNull());
        verify(classroomRepository).save(isA(Classroom.class));
        assertEquals("success", actualUpdateClassroomResult.getCode());
        assertEquals("success", actualUpdateClassroomResult.getMessage());
        assertSame(classroom, actualUpdateClassroomResult.getData());
    }

    /**
     * Test {@link ClassroomController#updateClassroom(Classroom, OidcUser)} with
     * {@code classroom}, {@code principal}.
     * <ul>
     *   <li>Then calls {@link ClassroomService#updateClassroom(Classroom)}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ClassroomController#updateClassroom(Classroom, OidcUser)}
     */
    @Test
    @DisplayName("Test updateClassroom(Classroom, OidcUser) with 'classroom', 'principal'; then calls updateClassroom"
            + "(Classroom)")
    void testUpdateClassroomWithClassroomPrincipal_thenCallsUpdateClassroom() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        ClassroomService service = mock(ClassroomService.class);
        Classroom classroom = new Classroom();
        when(service.updateClassroom(Mockito.any())).thenReturn(classroom);
        LoginUtil loginUtil = mock(LoginUtil.class);
        doNothing().when(loginUtil).log(Mockito.any(), Mockito.any(), Mockito.any());
        ClassroomController classroomController = new ClassroomController(service, loginUtil);

        // Act
        Response actualUpdateClassroomResult = classroomController.updateClassroom(new Classroom(), null);

        // Assert
        verify(service).updateClassroom(isA(Classroom.class));
        verify(loginUtil).log(eq("POST /classroom"), eq("Classroom(id=null, name=null, size=null, software=null)"),
                isNull());
        assertEquals("success", actualUpdateClassroomResult.getCode());
        assertEquals("success", actualUpdateClassroomResult.getMessage());
        assertSame(classroom, actualUpdateClassroomResult.getData());
    }

    /**
     * Test {@link ClassroomController#deleteClassroom(Integer)} with
     * {@code classroomId}.
     * <p>
     * Method under test: {@link ClassroomController#deleteClassroom(Integer)}
     */
    @Test
    @DisplayName("Test deleteClassroom(Integer) with 'classroomId'")
    void testDeleteClassroomWithClassroomId() {
        // Arrange
        Classroom classroom = new Classroom();
        when(classroomService.deleteClassroom(Mockito.<Integer>any())).thenReturn(classroom);

        // Act
        Response actualDeleteClassroomResult = classroomController.deleteClassroom(1);

        // Assert
        verify(classroomService).deleteClassroom(eq(1));
        assertEquals("success", actualDeleteClassroomResult.getCode());
        assertEquals("success", actualDeleteClassroomResult.getMessage());
        assertSame(classroom, actualDeleteClassroomResult.getData());
    }

    /**
     * Test {@link ClassroomController#deleteClassroom(Integer, OidcUser)} with
     * {@code classroomId}, {@code principal}.
     * <ul>
     *   <li>Then calls {@link CrudRepository#delete(Object)}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ClassroomController#deleteClassroom(Integer, OidcUser)}
     */
    @Test
    @DisplayName("Test deleteClassroom(Integer, OidcUser) with 'classroomId', 'principal'; then calls delete(Object)")
    void testDeleteClassroomWithClassroomIdPrincipal_thenCallsDelete() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        ClassroomRepository classroomRepository = mock(ClassroomRepository.class);
        doNothing().when(classroomRepository).delete(Mockito.any());
        Classroom classroom = new Classroom();
        Optional<Classroom> ofResult = Optional.of(classroom);
        when(classroomRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        ClassroomService service = new ClassroomService(classroomRepository);
        LoginUtil loginUtil = mock(LoginUtil.class);
        doNothing().when(loginUtil).log(Mockito.any(), Mockito.any(), Mockito.any());

        // Act
        Response actualDeleteClassroomResult = (new ClassroomController(service, loginUtil)).deleteClassroom(1, null);

        // Assert
        verify(loginUtil).log(eq("DELETE /classroom"), eq("1"), isNull());
        verify(classroomRepository).delete(isA(Classroom.class));
        verify(classroomRepository).findById(eq(1));
        assertSame(classroom, actualDeleteClassroomResult.getData());
    }

    /**
     * Test {@link ClassroomController#deleteClassroom(Integer, OidcUser)} with
     * {@code classroomId}, {@code principal}.
     * <ul>
     *   <li>Then calls {@link ClassroomService#deleteClassroom(Integer)}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ClassroomController#deleteClassroom(Integer, OidcUser)}
     */
    @Test
    @DisplayName("Test deleteClassroom(Integer, OidcUser) with 'classroomId', 'principal'; then calls deleteClassroom"
            + "(Integer)")
    void testDeleteClassroomWithClassroomIdPrincipal_thenCallsDeleteClassroom() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        ClassroomService service = mock(ClassroomService.class);
        Classroom classroom = new Classroom();
        when(service.deleteClassroom(Mockito.<Integer>any())).thenReturn(classroom);
        LoginUtil loginUtil = mock(LoginUtil.class);
        doNothing().when(loginUtil).log(Mockito.any(), Mockito.any(), Mockito.any());

        // Act
        Response actualDeleteClassroomResult = (new ClassroomController(service, loginUtil)).deleteClassroom(1, null);

        // Assert
        verify(service).deleteClassroom(eq(1));
        verify(loginUtil).log(eq("DELETE /classroom"), eq("1"), isNull());
        assertSame(classroom, actualDeleteClassroomResult.getData());
    }

    /**
     * Test {@link ClassroomController#deleteClassroom(Integer, OidcUser)} with
     * {@code classroomId}, {@code principal}.
     * <ul>
     *   <li>Then return Code is {@code success}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ClassroomController#deleteClassroom(Integer, OidcUser)}
     */
    @Test
    @DisplayName("Test deleteClassroom(Integer, OidcUser) with 'classroomId', 'principal'; then return Code is "
            + "'success'")
    void testDeleteClassroomWithClassroomIdPrincipal_thenReturnCodeIsSuccess() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        ClassroomRepository classroomRepository = mock(ClassroomRepository.class);
        Optional<Classroom> emptyResult = Optional.empty();
        when(classroomRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);
        ClassroomService service = new ClassroomService(classroomRepository);
        LoginUtil loginUtil = mock(LoginUtil.class);
        doNothing().when(loginUtil).log(Mockito.any(), Mockito.any(), Mockito.any());

        // Act
        Response actualDeleteClassroomResult = (new ClassroomController(service, loginUtil)).deleteClassroom(1, null);

        // Assert
        verify(loginUtil).log(eq("DELETE /classroom"), eq("1"), isNull());
        verify(classroomRepository).findById(eq(1));
        assertEquals("success", actualDeleteClassroomResult.getCode());
        assertEquals("success", actualDeleteClassroomResult.getMessage());
        assertNull(actualDeleteClassroomResult.getData());
    }
}
