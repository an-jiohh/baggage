package com.example.baggage.bungchan.controller;

import com.example.baggage.bungchan.service.BcTaxiService;
import com.example.baggage.dto.TaxiRequestDto;
import com.example.baggage.dto.TaxiPredictResponsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BcTaxiController {
    private final BcTaxiService service;
    @PostMapping("/taxi/predict")
    public TaxiPredictResponsDto predict(@RequestBody TaxiRequestDto requestDTO)
    {
        return service.calculateTaxiCost(requestDTO);
    }

}
