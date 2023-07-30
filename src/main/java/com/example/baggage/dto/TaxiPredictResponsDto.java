package com.example.baggage.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaxiPredictResponsDto {
    private  int usetime;
    private  int fee;
    private  double distance;
}
