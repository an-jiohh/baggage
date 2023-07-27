package com.example.baggage.jiho.controller;

import com.example.baggage.dto.ReportRequestDto;
import com.example.baggage.dto.ReportResponseDto;
import com.example.baggage.jiho.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TexiController {
    private final ReportService reportService;

    @PostMapping("/texi/report")
    public ReportResponseDto texiReport(@RequestBody ReportRequestDto request){
        String prompt = reportService.createPrompt(request);
        String report = reportService.createReport(request, prompt);
        log.info(report);
        return new ReportResponseDto(report);
    }

}