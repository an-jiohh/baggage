package com.example.baggage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportResponseDto {
    String prompt;

    public ReportResponseDto(String prompt) {
        this.prompt = prompt;
    }
}