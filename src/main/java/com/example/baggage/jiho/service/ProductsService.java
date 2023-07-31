package com.example.baggage.jiho.service;

import com.example.baggage.dto.ProductInnerDto;
import com.example.baggage.dto.ProductsDto;
import com.example.baggage.dto.ProductsRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductsService {

    @Value("${products.p_cert_key}")
    private String pCertKey;
    @Value("${products.p_cert_id}")
    private String pCertId;
    private String url = "https://www.kamis.or.kr/service/price/xml.do";


    private final ObjectMapper objectMapper;

    private final Map<String, String> cityMap = new HashMap<>();

    @PostConstruct
    private void initCityMap(){
        cityMap.put("1101", "서울");
        cityMap.put("2100", "부산");
        cityMap.put("2200", "대구");
        cityMap.put("2300", "인천");
        cityMap.put("2401", "광주");
        cityMap.put("2501", "대전");
        cityMap.put("2601", "울산");
        cityMap.put("3111", "수원");
        cityMap.put("3211", "춘천");
        cityMap.put("3311", "청주");
        cityMap.put("3511", "전주");
        cityMap.put("3711", "포항");
        cityMap.put("3911", "제주");
        cityMap.put("3113", "의정부");
        cityMap.put("3613", "순천");
        cityMap.put("3714", "안동");
        cityMap.put("3814", "창원");
        cityMap.put("3145", "용인");
    }

    public ProductInnerDto getProductInfo(ProductsRequestDto request) throws RuntimeException{
        String allItem = getItemInfo(request.getP_itemcategorycode(), request.getP_itemcode(), request.getP_kindcode(), "", request.getP_productclscode());

        ProductsDto allItemObject = productsDtoParser(allItem);

        List<ProductsDto.Item> items = allItemObject.getData().getItem();
        ProductsDto.Item allInfo = items.get(0); //첫번쨰가 평균
        ProductsDto.Item localInfo = items.get(0); //값이 없을 경우 평균으로 대치
        for (ProductsDto.Item item : items) {
            if(item.getCountyname().equals(cityMap.get(request.getP_countycode()))){
                System.out.println("item.toString() = " + item.toString());
                localInfo = item;
            }
        }

        ProductInnerDto productInnerDto = new ProductInnerDto();
        productInnerDto.setUserprice(Integer.parseInt(request.getUserprice()));

        //숫자사이 콤마, 값없을때 - 삭제
        int price = Integer.parseInt(localInfo.getPrice().replace(",", "").replace("-", ""));
        int weekPrice = Integer.parseInt(localInfo.getWeekprice().replace(",", "").replace("-", ""));
        int monthPrice = Integer.parseInt(localInfo.getMonthprice().replace(",", "").replace("-", ""));
        int nationalPrice = Integer.parseInt(allInfo.getPrice().replace(",", "").replace("-", ""));

        productInnerDto.setProductName(localInfo.getItemname());
        productInnerDto.setPrice(price);
        productInnerDto.setWeekprice(weekPrice);
        productInnerDto.setMonthprice(monthPrice);
        productInnerDto.setNationalprice(nationalPrice);

        System.out.println("productInnerDto = " + productInnerDto);
        return productInnerDto;
    }

    private ProductsDto productsDtoParser(String json){
        ProductsDto productsDto = null;
        try{
            productsDto = objectMapper.readValue(json, ProductsDto.class);
        } catch (MismatchedInputException e){
            log.error("no data");
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return productsDto;
    }

    private String getItemInfo(String p_itemcategorycode,String p_itemcode,String p_kindcode, String p_countycode, String p_productclscode){
        WebClient webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
        String report = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("action", "ItemInfo") //지역별 품목별 도.소매가격정보 API 선택
                        .queryParam("p_cert_key", pCertKey) //인증 key
                        .queryParam("p_cert_id", pCertId) //인증자 id
                        .queryParam("p_returntype", "json") //return type
                        .queryParam("p_productclscode", "0"+p_productclscode) //01:소매, 02:도매
                        .queryParam("p_itemcategorycode", p_itemcategorycode) //부류코드
                        .queryParam("p_itemcode", p_itemcode) //품목코드
                        .queryParam("p_kindcode", p_kindcode) //품종코드
                        //.queryParam("p_countycode", p_countycode)
                        .queryParam("p_convert_kg_yn", "Y") //kg단위 환산여부
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return report;
    }

    public String getCowItemInfo(String p_itemcode,String p_kindcode){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minusMonths= now.minusMonths(1);

        String now_format = now.format(dateTimeFormatter);
        String month_format = minusMonths.format(dateTimeFormatter);

        WebClient webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
        String report = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("action", "periodProductList") //지역별 품목별 도.소매가격정보 API 선택
                        .queryParam("p_startday", month_format)
                        .queryParam("p_endday", now_format)
                        .queryParam("p_cert_key", pCertKey) //인증 key
                        .queryParam("p_cert_id", pCertId) //인증자 id
                        .queryParam("p_returntype", "json") //return type
                        .queryParam("p_itemcode", p_itemcode) //품목코드
                        .queryParam("p_kindcode", p_kindcode) //품종코드
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return report;
    }


    public ProductInnerDto getCowProductInfo(ProductsRequestDto request) throws Exception {
        String cowItemInfo = getCowItemInfo(request.getP_itemcode(), request.getP_kindcode());
        JsonNode jsonNode = objectMapper.readTree(cowItemInfo);
        JsonNode jsonNode1 = jsonNode.get("data").get("item");
        List<JsonNode> items = new ArrayList<>();
        Iterator<JsonNode> elements = jsonNode1.elements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            if (next.get("countyname").asText().equals("전국")){
                items.add(next);
            }
        }

        int price = Integer.parseInt(items.get(items.size()-1).get("price").asText().replace(",", "").replace("-", ""));
        int weekPrice =  Integer.parseInt(items.get(items.size()-7).get("price").asText().replace(",", "").replace("-", ""));
        int monthPrice =  Integer.parseInt(items.get(0).get("price").asText().replace(",", "").replace("-", ""));
        int nationalPrice = Integer.parseInt(items.get(items.size()-1).get("price").asText().replace(",", "").replace("-", ""));

        ProductInnerDto productInnerDto = new ProductInnerDto();
        productInnerDto.setUserprice(Integer.parseInt(request.getUserprice()));

        productInnerDto.setProductName(items.get(0).get("itemname").asText());
        productInnerDto.setPrice(price);
        productInnerDto.setWeekprice(weekPrice);
        productInnerDto.setMonthprice(monthPrice);
        productInnerDto.setNationalprice(nationalPrice);

        return productInnerDto;
    }

}
