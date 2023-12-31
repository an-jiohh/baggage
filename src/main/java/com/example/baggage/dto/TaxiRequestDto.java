package com.example.baggage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxiRequestDto {
    private LocationDTO start;
    private LocationDTO end;


    @Setter
    @Getter
    public static class LocationDTO {
        private String latitude;
        private String longitude;
    }

}
