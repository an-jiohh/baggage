package com.example.baggage.bungchan.controller;

import com.example.baggage.dto.LodgmentDTO;
import com.example.baggage.dto.TaxiRequestDTO;
import com.example.baggage.dto.TexiPredict;
import com.example.baggage.bungchan.service.BcService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class BcController {
    private final BcService service;

    @PostMapping("/taxi/predict")
    public TexiPredict predict(@RequestBody TaxiRequestDTO requestDTO)
    {
        return service.calculateTaxiCost(requestDTO);
    }

    //    숙소 TEST
    @GetMapping("/lodgment/compare")
    public LodgmentDTO judgment(@RequestParam("name") String  name, @RequestParam ("region") String region){
        return service.lodgmentCompare(name ,region);
    }

}