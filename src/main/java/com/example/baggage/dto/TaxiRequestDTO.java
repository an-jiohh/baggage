package com.example.baggage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxiRequestDTO {
    private LocationDTO start;
    private LocationDTO end;
}
