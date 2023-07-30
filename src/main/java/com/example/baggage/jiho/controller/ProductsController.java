package com.example.baggage.jiho.controller;

import com.example.baggage.dto.ProductInnerDto;
import com.example.baggage.dto.ProductsRequestDto;
import com.example.baggage.dto.ProductsResponseDto;
import com.example.baggage.jiho.service.ProductsService;
import com.example.baggage.jungchan.service.gpt.GptService;
import com.example.baggage.temp.ProductCompareService;
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
            productInfo = new ProductInnerDto();
        }else{
            productInfo = productsService.getProductInfo(request);
        }

        //여기서 no data 인지 확인 -> return
        productInfo.setUserprice(Integer.parseInt(request.getUserprice()));

        //정찬쓰 코드 여기 입력
        ProductsResponseDto productsResponseDto = productCompareService.compareService(productInfo);

        return productsResponseDto;
    }
}
