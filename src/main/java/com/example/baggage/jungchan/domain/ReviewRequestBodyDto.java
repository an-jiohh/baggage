package com.example.baggage.jungchan.domain;

import lombok.Data;

@Data
public class ReviewRequestBodyDto {

    private String Region;
    private double rate;
    private String review;
}
