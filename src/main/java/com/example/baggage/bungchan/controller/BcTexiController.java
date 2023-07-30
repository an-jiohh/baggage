package com.example.baggage.bungchan.controller;

import com.example.baggage.bungchan.service.BcTexiService;
import com.example.baggage.dto.TaxiRequestDto;
import com.example.baggage.dto.TexiPredictResponsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BcTexiController {
    private final BcTexiService service;
    @PostMapping("/taxi/predict")
    public TexiPredictResponsDto predict(@RequestBody TaxiRequestDto requestDTO)
    {
        return service.calculateTaxiCost(requestDTO);
    }

}
