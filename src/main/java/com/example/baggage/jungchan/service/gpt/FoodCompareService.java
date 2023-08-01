package com.example.baggage.jungchan.service.gpt;

import com.example.baggage.dto.FoodRequestDto;
import com.example.baggage.dto.FoodResponseDto;
import com.example.baggage.dto.KaKaoResponseDto;
import com.example.baggage.dto.ProductInnerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodCompareService {

//    public List<KaKaoResponseDto>
static final String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
    @Value("${kakao.kakaoAK}")
    private String kakaoAk;

    public KaKaoResponseDto searchPlaceByKeyword(FoodRequestDto foodRequestDto){

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "KakaoAK " + kakaoAk)
                .build();


        KaKaoResponseDto kaKaoResponseDto = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("y", foodRequestDto.getLatitude())
                        .queryParam("x", foodRequestDto.getLongitude())
                        .queryParam("radius","10000")
                        .queryParam("size","15")
                        .queryParam("query", foodRequestDto.getName())
                        .build())
                .retrieve()
                .bodyToMono(KaKaoResponseDto.class)
                .block();

        return kaKaoResponseDto;

    }


    public void sortByRating(FoodResponseDto foodResponseDto){
        Collections.sort(foodResponseDto.getShoplist());
    }



    public int findMax(List<FoodResponseDto.ShopList> shopLists) {

        if (shopLists == null || shopLists.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty.");
        }
        int max = shopLists.get(0).getShopprice();

        for(int i=0; i <shopLists.size(); i++){
            int num = shopLists.get(i).getShopprice();
            if(num > max){
                max = num;
            }
        }
        return max;
    }

    public int findMin(List<FoodResponseDto.ShopList> shopLists) {
        if (shopLists == null || shopLists.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty.");
        }

        int min = shopLists.get(0).getShopprice();

        for(int i=0; i <shopLists.size(); i++){
            int num = shopLists.get(i).getShopprice();
            if(num < min){
                min = num;
            }
        }
        return min;

    }

    public int findAverage(List<FoodResponseDto.ShopList> shopLists) {
        if (shopLists == null || shopLists.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty.");
        }

        int sum = 0;
        for(int i=0; i <shopLists.size(); i++){
            sum += shopLists.get(i).getShopprice();
        }
        return sum / shopLists.size();
    }


    public String compareRank(FoodResponseDto productInnerDto, int price) {

        int avgPrice = (productInnerDto.getAvgprice() + productInnerDto.getNationalprice()) / 2;
        int userPrice = price;

        double priceGap = userPrice - avgPrice;
        double priceGapPercentage = (priceGap / avgPrice) * 100;

        String rank = null;

        //비교 로직
        if (priceGapPercentage < 0) {
            //저렴
            rank = "저렴";
        } else if (0 <= priceGapPercentage && priceGapPercentage < 10) {
            //적정
            rank = "적정";
        } else if (10 <= priceGapPercentage && priceGapPercentage < 20) {
            //의심
            rank = "의심";
        } else if (20 <= priceGapPercentage && priceGapPercentage < 30) {
            //위험
            rank = "위험";
        } else {
            //매우 위험
            rank = "매우 위험";
        }

        return rank;
    }
}
