package com.example.baggage.jungchan.controller;

import com.example.baggage.bungchan.service.CrSevice;
import com.example.baggage.dto.CompareRequestDto;
import com.example.baggage.dto.FoodResponseDto;
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
    public FoodResponseDto foodCompare(){ //정찬 = 파라미터 알아서 넣어주세요.
        //정찬, 병찬 = 파라미터 및 리턴값 조율

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
