package com.example.baggage.dto;

import lombok.Data;

@Data
public class FoodRequestDto {

    private String latitude;
    private String longitude;
    private String name;
    private int price;
}
