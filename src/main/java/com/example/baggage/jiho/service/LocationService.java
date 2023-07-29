package com.example.baggage.jiho.service;

import com.example.baggage.dto.KaKaoResponseDto;
import com.example.baggage.dto.KakaoAddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class LocationService {

    @Value("${kakao.kakaoAK}")
    private String kakaoAk;

    private String Addressurl = "https://dapi.kakao.com/v2/local/geo/coord2address";
    public KakaoAddressDto coordinateToAddress(String latitude, String longitude){
        WebClient webClient = WebClient.builder()
                .baseUrl(Addressurl)
                .defaultHeader("Authorization", "KakaoAK " + kakaoAk)
                .build();

        KakaoAddressDto kakaoAddressDto = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("y", latitude)
                        .queryParam("x", longitude)
                        .build())
                .retrieve()
                .bodyToMono(KakaoAddressDto.class)
                .block();

        return kakaoAddressDto;
    }
}
