package com.example.baggage.jiho.service;

import com.example.baggage.dto.ProductInnerDto;
import com.example.baggage.dto.ProductsDto;
import com.example.baggage.dto.ProductsRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductsService {

    @Value("${products.p_cert_key}")
    private String pCertKey;
    @Value("${products.p_cert_id}")
    private String pCertId;
    private String url = "http://www.kamis.or.kr/service/price/xml.do";

    private final ObjectMapper objectMapper;

    public ProductInnerDto getCowProductInfo(ProductsRequestDto requestDto){
        return new ProductInnerDto();
    }

    public ProductInnerDto getProductInfo(ProductsRequestDto request){
        String localItem = getItemInfo(request.getP_itemcategorycode(), request.getP_itemcode(), request.getP_kindcode(), "", request.getP_productclscode());
        String allItem = getItemInfo(request.getP_itemcategorycode(), request.getP_itemcode(), request.getP_kindcode(), "", request.getP_productclscode());

        ProductsDto localItemObject = productsDtoParser(localItem);
        ProductsDto allItemObject = productsDtoParser(allItem);

        List<ProductsDto.Item> items = localItemObject.getData().getItem();
        ProductsDto.Item localInfo = items.get(items.size() - 1);
        ProductInnerDto productInnerDto = new ProductInnerDto();
        productInnerDto.setUserprice(Integer.parseInt(request.getUserprice()));

        productInnerDto.setProductName(localInfo.getItemname());
        productInnerDto.setPrice(Integer.parseInt(localInfo.getPrice().replace(",","")));
        productInnerDto.setWeekprice(Integer.parseInt(localInfo.getWeekprice().replace(",","")));
        productInnerDto.setMonthprice(Integer.parseInt(localInfo.getMonthprice().replace(",","")));
        productInnerDto.setNationalprice(Integer.parseInt(allItemObject.getData().getItem().get(0).getPrice().replace(",","")));

        System.out.println("productInnerDto = " + productInnerDto);
        return productInnerDto;
    }

    public ProductsDto productsDtoParser(String json){
        ProductsDto productsDto = null;
        try{
            productsDto = objectMapper.readValue(json, ProductsDto.class);
        } catch (MismatchedInputException e){
            log.error("no data");
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return productsDto;
    }

    public String getItemInfo(String p_itemcategorycode,String p_itemcode,String p_kindcode,String p_countycode, String p_productclscode){
        WebClient webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
        ClientResponse report = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("action", "ItemInfo") //지역별 품목별 도.소매가격정보 API 선택
                        .queryParam("p_cert_key", pCertKey) //인증 key
                        .queryParam("p_cert_id", pCertId) //인증자 id
                        .queryParam("p_returntype", "json") //return type
                        .queryParam("p_productclscode", "0"+p_productclscode) //01:소매, 02:도매
                        //.queryParam("p_regday", "") //조회날짜 -> 생략하면 오늘
                        .queryParam("p_itemcategorycode", p_itemcategorycode) //부류코드
                        .queryParam("p_itemcode", p_itemcode) //품목코드
                        .queryParam("p_kindcode", p_kindcode) //품종코드
                        //.queryParam("p_productrankcode","{url}") //등급코드 -> 생략
                        //.queryParam("p_countycode", p_countycode) //시군구코드
                        .queryParam("p_convert_kg_yn", "Y") //kg단위 환산여부
                        .build())
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is3xxRedirection()) {
                        // 3xx Redirection 처리
                        String newUrl = clientResponse.headers().header("Location").get(0);
                        return webClient.get().uri(newUrl).exchange();
                    } else {
                        return Mono.just(clientResponse);
                    }
                })
                .block();

        return report.bodyToMono(String.class).block();
    }

    public String getCowItemInfo(){
        WebClient webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
        ClientResponse report = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("action", "ItemInfo") //지역별 품목별 도.소매가격정보 API 선택
                        .queryParam("p_cert_key", pCertKey) //인증 key
                        .build())
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is3xxRedirection()) {
                        // 3xx Redirection 처리
                        String newUrl = clientResponse.headers().header("Location").get(0);
                        return webClient.get().uri(newUrl).exchange();
                    } else {
                        return Mono.just(clientResponse);
                    }
                })
                .block();

        return report.bodyToMono(String.class).block();
    }
}
