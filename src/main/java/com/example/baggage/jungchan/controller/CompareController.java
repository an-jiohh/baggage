package com.example.baggage.jungchan.controller;

import com.example.baggage.bungchan.service.BcCrawlingSevice;
import com.example.baggage.dto.*;
import com.example.baggage.jiho.service.FoodNationalService;
import com.example.baggage.jiho.service.LocationService;
import com.example.baggage.jiho.service.MobomService;
import com.example.baggage.jungchan.service.gpt.FoodCompareService;
import com.example.baggage.jungchan.service.gpt.GptService;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompareController {

    private final ChatgptService chatgptService;
    private final GptService gptService;
    private final FoodCompareService foodCompareService;
    private final BcCrawlingSevice bcCrawlingSevice;
    private final FoodNationalService foodNationalService;
    private final LocationService locationService;
    private final MobomService mobomService;

    @PostMapping("/taxi/compare")
    public CompareResponse taxiCompare(@RequestBody @Valid CompareRequestDto compareRequestDto){

        //Request바디의 값을 받아서 매핑
        double realDistance = compareRequestDto.getReal().getDistance();
        int realFee = compareRequestDto.getReal().getFee();
        int realUsetime = compareRequestDto.getReal().getUsetime();

        double predictDistance = compareRequestDto.getPredict().getDistance();
        int predictFee = compareRequestDto.getPredict().getFee();
        int predictUsetime = compareRequestDto.getPredict().getUsetime();

        //매핑된 값을 chatGPT에게 넘겨줌
        String parameter =  gptService.createTaxiPrompt(realDistance, realFee, realUsetime, predictDistance, predictFee, predictUsetime);

        String prompt = chatgptService.multiChat(Arrays.asList(new MultiChatMessage("user",parameter)));

        return new CompareResponse(prompt);
    }

    @PostMapping("/food/compare")
    public FoodResponseDto foodCompare(@RequestBody @Valid FoodRequestDto foodRequestDto){ //정찬 = 파라미터 알아서 넣어주세요.
        //정찬, 병찬 = 파라미터 및 리턴값 조율
        KaKaoResponseDto kaKaoResponseDto = foodCompareService.searchPlaceByKeyword(foodRequestDto);

        System.out.println("@@@@@@TEST@@@@@@@");
        //KakaoResponsDto.getDocuments가 리스트로 이루어져있음 그 안에 매장명, 매장 ID(플레이스 뒤에 붙는 숫자), 매장 주소 들어있음.
        for(int i=0; i < kaKaoResponseDto.getDocuments().size(); i++){
            System.out.println(kaKaoResponseDto.getDocuments().get(i).toString());
        }

        //병찬 = 크롤링
        FoodResponseDto foodResponseDto =  bcCrawlingSevice.crawling(kaKaoResponseDto, foodRequestDto.getName());


        //정찬
        //최소가격, 최대가격, 평균가격 + 값 안들어 왔을 때 예외처리
        int max = 0;
        int min = 0;
        int avg = 0;
        try {
            max = foodCompareService.findMax(foodResponseDto.getShoplist());
            min = foodCompareService.findMin(foodResponseDto.getShoplist());
            avg = foodCompareService.findAverage(foodResponseDto.getShoplist());
        }catch (IllegalArgumentException e){
        }

        foodResponseDto.setMaxprice(max);
        foodResponseDto.setMinprice(min);
        foodResponseDto.setAvgprice(avg);

        //평점순으로 리스트 나열
        foodCompareService.sortByRating(foodResponseDto);
        System.out.println("foodRequestDto.getName() = " + foodRequestDto.getName());

        //locationalprice 정보주입
        foodResponseDto.setNationalprice(foodNationalService.getNationalPrice(foodRequestDto.getName()));

        //gpt 에게 평가 (return prompt)
        String prompt = gptService.createFoodPrompt(foodResponseDto,foodRequestDto.getPrice());   //nationalprice, maxprice, minprice
        String resultPrompt = chatgptService.multiChat(Arrays.asList(new MultiChatMessage("user",prompt)));

        foodResponseDto.setPrompt(resultPrompt);

        //지호 주요로직
        KakaoAddressDto kakaoAddressDto = locationService.coordinateToAddress(foodRequestDto.getLatitude(),foodRequestDto.getLongitude());
        String address = "0000 000 000"; //없을 경우를 대비 실존하지 않는 값 대입
        address = locationService.getRegion123Merge(kakaoAddressDto);

        List<FoodResponseDto.MobomList> mobomLists = mobomService.getMobomData(address, foodRequestDto.getName());
        foodResponseDto.setMobomlist(mobomLists);

        return foodResponseDto;
    }

    @Data
    @AllArgsConstructor
    public static class CompareResponse{
        private String prompt;
    }
}
