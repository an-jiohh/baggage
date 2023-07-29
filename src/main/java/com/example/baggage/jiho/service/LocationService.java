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

    public String getRegion123Merge(KakaoAddressDto kakaoAddressDto){//도 + 시 + 동 정보가져오는 함수
        String address = null;
        if(kakaoAddressDto.getMeta().getTotal_count() == 1){ //값이 존재하면
            KakaoAddressDto.Document document = kakaoAddressDto.getDocuments()[0];
            if(document.getAddress() != null){
                String region1depthName = document.getAddress().getRegion_1depth_name();
                String region2depthName = document.getAddress().getRegion_2depth_name();
                String region3depthName = document.getAddress().getRegion_3depth_name();
                address = String.format(region1depthName + " " + region2depthName + " " + region3depthName);
            }
        }
        return address;
    }
}
