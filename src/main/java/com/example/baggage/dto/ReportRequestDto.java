package com.example.baggage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {
    private String carnum;
    private UsageData real;
    private UsageData predict;

    @Getter
    @Setter
    public static class UsageData {
        private int usetime;
        private int fee;
        private int distance;
    }

}
