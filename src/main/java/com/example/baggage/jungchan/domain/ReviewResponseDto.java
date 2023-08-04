package com.example.baggage.jungchan.domain;


import lombok.Data;

import java.util.List;

@Data
public class ReviewResponseDto {
    private double avgrate;
    List<ReviewList> reviewlist;

    @Data
    public static class ReviewList{
        private double rate;
        private String review;

    }


}
