package com.example.baggage.jungchan.service.gpt;

import com.example.baggage.dto.FoodRequestDto;
import com.example.baggage.dto.KaKaoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class FoodCompareService {

//    public List<KaKaoResponseDto>
    static final String url = "https://dapi.kakao.com/v2/local/search/keyword.json";


    public KaKaoResponseDto searchPlaceByKeyword(FoodRequestDto foodRequestDto){

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "KakaoAK 216c010df8a27d34179fba35e0a162e7")
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






}
