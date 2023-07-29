package com.example.baggage.bungchan.controller;

import com.example.baggage.bungchan.service.BcCrawlingSevice;

import com.example.baggage.dto.FoodResponseDto;
import com.example.baggage.dto.KaKaoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BcCrawlingTestController {
    private final BcCrawlingSevice service;


//   @@@@@@@@@@@@@@@@TEST@@@@@@@@@@@@@@@@
    @GetMapping("/test123")
    public String test()
    {

        KaKaoResponseDto test = new KaKaoResponseDto();
        KaKaoResponseDto.Document document = new KaKaoResponseDto.Document();
        document.setId("10719333");
        document.setPlace_name("비행장정문부대찌개");
        document.setRoad_address_name("전북 군산시 하나운안2길");


        KaKaoResponseDto.Document document1 = new KaKaoResponseDto.Document();
        document1.setId("23120124");
        document1.setPlace_name("궁전매운탕");
        document1.setRoad_address_name("전북 군산시 현충로 12");


        KaKaoResponseDto.Document document2 = new KaKaoResponseDto.Document();
        document2.setId("1700781495");
        document2.setPlace_name("신자네연탄구이");
        document2.setRoad_address_name("전북 군산시 부곡1길 17");

        List<KaKaoResponseDto.Document> documents = new ArrayList<>();
        documents.add(document);
        documents.add(document1);
        documents.add(document2);

        test.setDocuments(documents);

        FoodResponseDto test1 = service.crawling(test);
        System.out.println(test1.toString());
        return null;
    }


}
