package com.example.baggage.bungchan.controller;

import com.example.baggage.bungchan.service.BcLodgmentService;
import com.example.baggage.domain.Lodgment;
import com.example.baggage.dto.LodgmentResponsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class BcLodgmentController {
    private final BcLodgmentService service;
        @GetMapping("/lodgment/compare")
        public Lodgment compare (@RequestParam("name") String  name, @RequestParam ("region") String region){
            return service.lodgmentCompare(name ,region);
    }

    @GetMapping("/lodgment/offer")
    public LodgmentResponsDto offer ( @RequestParam ("region") String region){
            return service.lodgmentOffer(region);
    }

}
