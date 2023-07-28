package com.example.baggage.jungchan.service.gpt;

import com.example.baggage.dto.FoodRequestDto;
import com.example.baggage.dto.KaKaoResponseDto;
import org.junit.jupiter.api.Test;

class FoodCompareServiceTest {

    @Test
    void searchPlaceByKeyword() {
        FoodCompareService foodCompareService = new FoodCompareService();
        FoodRequestDto foodRequestDto = new FoodRequestDto();
        foodRequestDto.setLongitude("127.06283102249932");
        foodRequestDto.setLatitude("37.514322572335935");
        foodRequestDto.setName("곱창");
        foodRequestDto.setPrice(10000);

        KaKaoResponseDto kaKaoResponseDto = foodCompareService.searchPlaceByKeyword(foodRequestDto);

        System.out.println("@@@@@@TEST@@@@@@@");
        for(int i=0; i < kaKaoResponseDto.getDocuments().size(); i++){
            System.out.println(kaKaoResponseDto.getDocuments().get(i).toString());
        }
    }
}