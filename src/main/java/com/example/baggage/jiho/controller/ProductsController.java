package com.example.baggage.jiho.controller;

import com.example.baggage.dto.ProductInnerDto;
import com.example.baggage.dto.ProductsRequestDto;
import com.example.baggage.jiho.service.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductsController {

    private final ProductsService productsService;
    @PostMapping("/products/compare")
    public String compareProducts(@RequestBody ProductsRequestDto request){
        if(request.getP_itemcategorycode().equals("500")) {
            ProductInnerDto productInfo = productsService.getCowProductInfo(request);
        }else{
            ProductInnerDto productInfo = productsService.getProductInfo(request);
        }

        //여기서 no data 인지 확인 -> return

        //정찬쓰 코드 여기 입력

        return "OK";
    }
}
