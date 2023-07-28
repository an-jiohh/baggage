package com.example.baggage.jiho.service;

import com.example.baggage.dto.FoodRequestDto;
import com.example.baggage.dto.KakaoAddressDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FoodNationalServiceTest {

    @Autowired
    private FoodNationalService foodNationalService;

    @Autowired
    private LocationService locationService;

    @Test
    void getNationalPrice() {
        FoodRequestDto foodRequestDto = new FoodRequestDto();

        foodRequestDto.setName("백반"); //있으면 그대로 리턴
        int resultPrice = 12750;
        Assertions.assertThat(foodNationalService.getNationalPrice(foodRequestDto)).isEqualTo(resultPrice);

        foodRequestDto.setName("회"); //실존값 : 모둠회, 검색값 : 회 -> 비슷한거있으면 비슷한것으로
        resultPrice = 38651;
        Assertions.assertThat(foodNationalService.getNationalPrice(foodRequestDto)).isEqualTo(resultPrice);

        foodRequestDto.setName("똥"); //없으면 0리턴
        resultPrice = 0;
        Assertions.assertThat(foodNationalService.getNationalPrice(foodRequestDto)).isEqualTo(resultPrice);

        //
        KakaoAddressDto kakaoAddressDto = locationService.coordinateToAddress("35.945529210679155", "126.68047569999999");
        kakaoAddressDto.toString();


        foodNationalService.getNationalPrice(foodRequestDto);
    }
}