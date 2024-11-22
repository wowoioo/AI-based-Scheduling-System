package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.entity.Response;
import com.github.rayinfinite.scheduler.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Response getData(String startStr, String endStr) throws ParseException {
        List<InputData> data = service.findByCourseDateBetween(startStr, endStr);
        return new Response(data);
    }
}
