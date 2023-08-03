package com.example.baggage.bungchan.service;

import com.example.baggage.domain.Lodgment;
import com.example.baggage.dto.LodgmentRequestDto;
import com.example.baggage.dto.LodgmentResponsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BcLodgmentService {
    @PersistenceContext
    private EntityManager em;


    public Lodgment lodgmentCompare(String name, String region){

        String query = "SELECT license , address , name FROM LODGING "
                + "WHERE address LIKE CONCAT('%', :region, '%') AND name = :name";

        List<Lodgment> result = em.createNativeQuery(query)
                .setParameter("region", region)
                .setParameter("name", name)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(Lodgment.class))
                .getResultList();

        if (!result.isEmpty())
        {
            result.get(0).setCode(0);
            return result.get(0);
        }else {
            Lodgment lodgment = new Lodgment();
            lodgment.setCode(1);
            return lodgment;
        }

    }


    @Value("${lodgment.url}")
    public String BASE_URL;

    @Value("${lodgment.api-key}")
    public String API_KEY;
    public LodgmentResponsDto  lodgmentOffer (String region)
    {
        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();

        String  response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "test")
                        .queryParam("_type", "json")
                        .queryParam("keyword", region)
                        .queryParam("contentTypeId", "32")
                        .queryParam("serviceKey", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

//        System.out.println(response);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LodgmentRequestDto  lodgmentRequestDto = objectMapper.readValue(response, LodgmentRequestDto.class);
            System.out.println(lodgmentRequestDto.toString());

            LodgmentResponsDto lodgmentResponseDto = new LodgmentResponsDto();
            List<LodgmentResponsDto.LodgmentList> lodgmentList = new ArrayList<>();
            for (LodgmentRequestDto.Item item : lodgmentRequestDto.getResponse().getBody().getItems().getItem()) {
                LodgmentResponsDto.LodgmentList lodgment = new LodgmentResponsDto.LodgmentList();
                lodgment.setAdd1(item.getAddr1());
                lodgment.setAdd2(item.getAddr2());
                lodgment.setTitle(item.getTitle());
                lodgmentList.add(lodgment);
            }
            lodgmentResponseDto.setCode(0);
            lodgmentResponseDto.setLodgmentlist(lodgmentList);
            return lodgmentResponseDto;
        } catch (JsonProcessingException e) {
            LodgmentResponsDto lodgmentResponseDto = new LodgmentResponsDto();
            lodgmentResponseDto.setCode(1);
            System.out.println("Json 변환 오류 ");

            return lodgmentResponseDto;
        }
    }
}
