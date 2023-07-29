package com.example.baggage.bungchan.controller;

import com.example.baggage.bungchan.service.BcLodgmentService;
import com.example.baggage.bungchan.service.BcTexiService;
import com.example.baggage.dto.LodgmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class BcLodgmentController {
    private final BcLodgmentService service;
        @GetMapping("/lodgment/compare")
        public LodgmentDTO compare (@RequestParam("name") String  name, @RequestParam ("region") String region){
            return service.lodgmentCompare(name ,region);


    }
}
