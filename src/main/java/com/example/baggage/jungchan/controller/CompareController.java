package com.example.baggage.jungchan.controller;

import com.example.baggage.dto.CompareRequestDto;
import com.example.baggage.dto.FoodRequestDto;
import com.example.baggage.dto.FoodResponseDto;
import com.example.baggage.dto.KaKaoResponseDto;
import com.example.baggage.jungchan.service.gpt.FoodCompareService;
import com.example.baggage.jungchan.service.gpt.GptService;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class CompareController {

    private final ChatgptService chatgptService;
    private final GptService gptService;

    @PostMapping("/taxi/compare")
    public CompareResponse taxiCompare(@RequestBody @Valid CompareRequestDto compareRequestDto){

        //Request바디의 값을 받아서 매핑
        int realDistance = compareRequestDto.getReal().getDistance();
        int realFee = compareRequestDto.getReal().getFee();
        int realUsetime = compareRequestDto.getReal().getUsetime();

        int predictDistance = compareRequestDto.getPredict().getDistance();
        int predictFee = compareRequestDto.getPredict().getFee();
        int predictUsetime = compareRequestDto.getPredict().getUsetime();

        //매핑된 값을 chatGPT에게 넘겨줌
        String parameter =  gptService.createTaxiPrompt(realDistance, realFee, realUsetime, predictDistance, predictFee, predictUsetime);;

        String prompt = chatgptService.multiChat(Arrays.asList(new MultiChatMessage("user",parameter)));

        return new CompareResponse(prompt);
    }

    @PostMapping("/food/compare")
    public FoodResponseDto foodCompare(@RequestBody @Valid FoodRequestDto foodRequestDto){ //정찬 = 파라미터 알아서 넣어주세요.
        //정찬, 병찬 = 파라미터 및 리턴값 조율
        FoodCompareService foodCompareService = new FoodCompareService();
        KaKaoResponseDto kaKaoResponseDto = foodCompareService.searchPlaceByKeyword(foodRequestDto);

        System.out.println("@@@@@@TEST@@@@@@@");
        //KakaoResponsDto.getDocuments가 리스트로 이루어져있음 그 안에 매장명, 매장 ID(플레이스 뒤에 붙는 숫자), 매장 주소 들어있음.
        for(int i=0; i < kaKaoResponseDto.getDocuments().size(); i++){
            System.out.println(kaKaoResponseDto.getDocuments().get(i).toString());
        }



        //병찬 = 크롤링

        //정찬이형 주요로직

        //지호 주요로직

        return new FoodResponseDto();
    }

    @Data
    @AllArgsConstructor
    public static class CompareResponse{
        private String prompt;
    }
}
