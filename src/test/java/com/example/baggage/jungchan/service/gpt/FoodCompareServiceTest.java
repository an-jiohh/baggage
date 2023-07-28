package com.example.baggage.jungchan.service.gpt;

import com.example.baggage.dto.FoodRequestDto;
import com.example.baggage.dto.FoodResponseDto;
import com.example.baggage.dto.KaKaoResponseDto;
import com.example.baggage.jungchan.controller.CompareController;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)

class FoodCompareServiceTest {

    @Autowired  ChatgptService chatgptService;
    @Autowired GptService gptService;

    @Test
    void 키워드_장소_검색_API_받아오기() {
        FoodCompareService foodCompareService = new FoodCompareService();
        FoodRequestDto foodRequestDto = new FoodRequestDto();
        foodRequestDto.setLongitude("127.06283102249932");
        foodRequestDto.setLatitude("37.514322572335935");
        foodRequestDto.setName("곱창");
        foodRequestDto.setPrice(10000);

        KaKaoResponseDto kaKaoResponseDto = foodCompareService.searchPlaceByKeyword(foodRequestDto);

        //리스트로
        System.out.println("@@@@@@TEST@@@@@@@");
        for(int i=0; i < kaKaoResponseDto.getDocuments().size(); i++){
            System.out.println(kaKaoResponseDto.getDocuments().get(i).toString());
        }
    }

    @Test
    void 평점순정렬() throws NullPointerException{

        FoodResponseDto foodResponseDto = new FoodResponseDto();
        List<FoodResponseDto.ShopList> shopLists = new ArrayList<>();
        FoodCompareService foodCompareService = new FoodCompareService();

        for(int i=5; i>0; i--){
            FoodResponseDto.ShopList shop = new FoodResponseDto.ShopList();
            shop.setShopname("asd");
            shop.setShopmenu("asf");
            shop.setShopprice(10000);
            shop.setScore(String.format("%d",i));
            shopLists.add(shop);

        }
        //정렬전
        foodResponseDto.setShoplist(shopLists);
        System.out.println(foodResponseDto.getShoplist());
        //정렬후
        foodCompareService.sortByRating(foodResponseDto);
        System.out.println(foodResponseDto.getShoplist());


    }
    @Test
    void GPT_음식_질문() {
        FoodResponseDto foodResponseDto = new FoodResponseDto();
        foodResponseDto.setAvgprice(10000);
        foodResponseDto.setMinprice(8000);
        foodResponseDto.setMaxprice(15000);
        foodResponseDto.setNationalprice(13000);

        String prompt = gptService.createFoodPrompt(foodResponseDto,12000);   //nationalprice, maxprice, minprice
        String resultPrompt = chatgptService.multiChat(Arrays.asList(new MultiChatMessage("user",prompt)));


    }

    @Test
    void 최대_최소_평균_가격(){

        List<FoodResponseDto.ShopList> shopLists = new ArrayList<>();
        FoodResponseDto foodResponseDto = new FoodResponseDto();

        for(int i=10; i>0; i--){
            FoodResponseDto.ShopList shop = new FoodResponseDto.ShopList();
            shop.setShopname("asd");
            shop.setShopmenu("asf");
            shop.setShopprice(10000 + i);
            shop.setScore(String.format("%d",i));
            shopLists.add(shop);

        }

        foodResponseDto.setShoplist(shopLists);
        System.out.println(foodResponseDto.getShoplist());

        FoodCompareService foodCompareService = new FoodCompareService();
        int max =foodCompareService.findMax(shopLists);
        int min =foodCompareService.findMin(shopLists);
        int avg =foodCompareService.findAverage(shopLists);

        System.out.println("max:" +max +"min:" +  min + "avg:" + avg);

    }
}