package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.entity.Response;
import com.github.rayinfinite.scheduler.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AppController {
    private final AppService service;

    @PostMapping("/upload")
    public Response getExcel(MultipartFile file) throws IOException {
        String result = service.upload(file);
        return Response.builder().data(result).build();
    }

    @GetMapping("/data")
    public Response getData(String startStr, String endStr,
                            @RequestParam(required = false) List<String> teachers) throws ParseException {
        List<InputData> data = service.findByCourseDateBetween(startStr, endStr, teachers);
        return new Response(data);
    }

    @GetMapping("/teacher")
    public Response getAllTeachers() {
        List<String> data = service.getAllTeachers();
        return new Response(data);
    }

    @GetMapping("/classroom")
    public Response getAllClassrooms() {
        List<Classroom> data = service.getAllClassrooms();
        return new Response(data);
    }

    @PostMapping("/classroom")
    public Response updateClassroom(Classroom classroom) {
        var result = service.updateClassroom(classroom);
        return new Response(result);
    }

    @DeleteMapping("/classroom/{id}")
    public Response deleteClassroom(@PathVariable("id") Long classroomId) {
        var result = service.deleteClassroom(classroomId);
        return new Response(result);
    }
}
