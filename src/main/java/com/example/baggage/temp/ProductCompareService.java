package com.example.baggage.temp;

import com.example.baggage.dto.ProductInnerDto;

import com.example.baggage.dto.ProductsResponseDto;
import com.example.baggage.jungchan.service.gpt.GptService;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@RequiredArgsConstructor
@RestController
public class ProductCompareService {


    private final ChatgptService chatgptService;


    public ProductsResponseDto compareService(ProductInnerDto productInnerDto) {
        //1. 바가지 판단(rank) 로직
        String rank = compareRank(productInnerDto);

        //2. gpt 판단(prompt) 로직
        GptService gptService = new GptService();
        String inputPrompt = gptService.createProductPrompt(productInnerDto);
        String resultPrompt = chatgptService.multiChat(Arrays.asList(new MultiChatMessage("user", inputPrompt)));

        return (new ProductsResponseDto(resultPrompt, rank,
                productInnerDto.getPrice(), productInnerDto.getWeekprice(),
                productInnerDto.getUserprice(), productInnerDto.getMonthprice(), productInnerDto.getNationalprice()));
    }


    public String compareRank(ProductInnerDto productInnerDto) {

        int avgPrice = (productInnerDto.getPrice() + productInnerDto.getWeekprice() + productInnerDto.getMonthprice()) / 3;
        int userPrice = productInnerDto.getUserprice();

        int priceGap = userPrice - avgPrice;
        double priceGapPercentage = (priceGap / avgPrice) * 100;

        String rank = null;

        //비교 로직
        if (priceGapPercentage < 0) {
            //저렴
            rank = "저렴";
        } else if (0 <= priceGapPercentage && priceGapPercentage < 10) {
            //적정
            rank = "적정";
        } else if (10 <= priceGapPercentage && priceGapPercentage < 20) {
            //의심
            rank = "의심";
        } else if (20 <= priceGapPercentage && priceGapPercentage < 30) {
            //위험
            rank = "위험";
        } else {
            //매우 위험
            rank = "매우 위험";
        }

        return rank;
    }
}
