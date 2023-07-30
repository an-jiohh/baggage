package com.example.baggage.jiho.controller;

import com.example.baggage.dto.ProductInnerDto;
import com.example.baggage.dto.ProductsDto;
import com.example.baggage.dto.ProductsRequestDto;
import com.example.baggage.dto.ProductsResponseDto;
import com.example.baggage.jiho.service.ProductsService;
import com.example.baggage.jungchan.service.gpt.GptService;
import com.example.baggage.temp.ProductCompareService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductsController {

    private final GptService gptService;
    private final ProductCompareService productCompareService;

    private final ProductsService productsService;
    @PostMapping("/products/compare")
    public ProductsResponseDto compareProducts(@RequestBody ProductsRequestDto request){
        ProductInnerDto productInfo = null;
        if(request.getP_itemcategorycode().equals("500")) {
            //ProductInnerDto productInfo = productsService.getCowProductInfo(request);
            return new ProductsResponseDto(); //일단 null 출력
        }else{
            try{
                productInfo = productsService.getProductInfo(request);
            }catch (RuntimeException e){ //no_Data일때 리턴
                log.warn("No_data");
                ProductsResponseDto responseNodata = new ProductsResponseDto();
                responseNodata.setPrompt("데이터가 없습니다.");
                responseNodata.setRank("데이터가 없습니다.");
                responseNodata.setUserprice(Integer.parseInt(request.getUserprice()));
                return responseNodata;
            }
        }
        productInfo.setUserprice(Integer.parseInt(request.getUserprice()));


        //정찬쓰 코드 여기 입력
        ProductsResponseDto productsResponseDto = productCompareService.compareService(productInfo);

        return productsResponseDto;
    }
}
