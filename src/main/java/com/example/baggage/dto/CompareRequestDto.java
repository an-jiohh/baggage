package com.example.baggage.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
@Getter
public class CompareRequestDto {
    private Real real;
    private Predict predict;

    @Getter
    public static class Real{

        private int usetime;
        private int fee;
        private double distance;
    }
    @Getter
    public static class Predict{
        private int usetime;
        private int fee;
        private double distance;

    }
}
