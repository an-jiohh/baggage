package com.example.baggage.bungchan.service;

import com.example.baggage.dto.KakaoBuildDto;
import com.example.baggage.dto.TaxiRequestDto;
import com.example.baggage.dto.TaxiPredictResponsDto;
import com.example.baggage.jiho.service.LocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Service
@RequiredArgsConstructor
public class BcTaxiService {
    private final ObjectMapper objectMapper;
    private  final LocationService locationService;
    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.clientSecret}")
    private String clientSecret;

    public TaxiPredictResponsDto calculateTaxiCost(TaxiRequestDto requestDTO)
    {
        TaxiRequestDto.LocationDTO startLocation = requestDTO.getStart();
        TaxiRequestDto.LocationDTO endLocation = requestDTO.getEnd();
        //경도 위도 순

        //시작 위치(경도 위도)
        String start = startLocation.getLongitude()+','+startLocation.getLatitude();
        String end = endLocation.getLongitude()+','+endLocation.getLatitude();


//        System.out.println("start :"+start);
//        System.out.println("end :"+end);
        //  요청 전체 주소 예시, : https:/naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=126.8990034,27.1234&goal=126.8990034,27.224
        String naverUrl = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";


        //실시간 빠른길
        String option = "trafast";

        //SSL 보안
        HttpClient httpClient = HttpClient.create().secure(t -> {

            try {

                t.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build());

            } catch (SSLException e) {

                System.out.println(e);

            }
        });

        WebClient client = WebClient.builder()
                .baseUrl(naverUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        String response = client.get().uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("start", start)
                        .queryParam("goal", end)
                        .queryParam("option",option)
                        .build())
                .header("X-NCP-APIGW-API-KEY-ID",clientId)
                .header("X-NCP-APIGW-API-KEY",clientSecret)
                .retrieve() 			  // 응답을 받게 하되,
                .bodyToMono(String.class) // 응답 값을 하나만,
                .block(); 				  // 동기로 받으려 한다.

        // Result Code 가 0이 아니면 탐색 실패,,, 처리 해줘야함
        System.out.println("response = " + response);
        try {
            JsonNode jsonNode = objectMapper.readTree(response);

            //거리
            double distance = jsonNode.get("route").get("trafast").get(0).get("summary").get("distance").asDouble();
            //소수점 2자리 반올림
            distance = Math.round(distance/10)/100.0;

            //시간 분 단위
            int duration = (jsonNode.get("route").get("trafast").get(0).get("summary").get("duration").asInt())/60000;

            //택시 요금
            int taxiFare = jsonNode.get("route").get("trafast").get(0).get("summary").get("taxiFare").asInt();

            KakaoBuildDto startkakaoBuildDto = locationService.coordinateToBuildName(startLocation.getLatitude(), startLocation.getLongitude());
            String startBuilding = locationService.getRegionBuilding(startkakaoBuildDto);

            KakaoBuildDto endkakaoBuildDto = locationService.coordinateToBuildName(endLocation.getLatitude(), endLocation.getLongitude());
            String endBuilding = locationService.getRegionBuilding(endkakaoBuildDto);

            TaxiPredictResponsDto returnDTO = new TaxiPredictResponsDto(duration,taxiFare,distance,startBuilding,endBuilding);
            return returnDTO;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
