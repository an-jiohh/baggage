package com.example.baggage.jiho.service;

import com.example.baggage.domain.Mobom;
import com.example.baggage.dto.FoodRequestDto;
import com.example.baggage.dto.FoodResponseDto;
import com.example.baggage.dto.KakaoAddressDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FoodNationalServiceTest {

    @Autowired
    private FoodNationalService foodNationalService;

    @Autowired
    private LocationService locationService;
    @Autowired
    private MobomService mobomService;

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

        foodNationalService.getNationalPrice(foodRequestDto);
    }

    @Test
    void kakaoAdressTest(){
        KakaoAddressDto kakaoAddressDto = locationService.coordinateToAddress("37.5546", "126.9754");
        System.out.println(kakaoAddressDto.toString());
    }

    @Test
    void $getMobomTest(){
        //response 처리
        String x = "35.9617";
        String y = "126.9773";
        String TestMenu = "치킨";

        String address = "111111111"; //값을 검색해도 안나오게 임의의 값
        KakaoAddressDto kakaoAddressDto = locationService.coordinateToAddress(x, y);
        address = locationService.getRegion123Merge(kakaoAddressDto);

        System.out.println("address = " + address);

        List<FoodResponseDto.MobomList> mobomLists = mobomService.getMobomData(address, TestMenu);
        
        FoodResponseDto foodResponseDto = new FoodResponseDto();
        foodResponseDto.setMobomlist(mobomLists);

        for (FoodResponseDto.MobomList mobomList : foodResponseDto.getMobomlist()) {
            System.out.println("mobomList.getHeadmenu() = " + mobomList.getHeadmenu());
        }
    }
}