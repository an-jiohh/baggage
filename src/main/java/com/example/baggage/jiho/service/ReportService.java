package com.example.baggage.jiho.service;

import com.example.baggage.dto.ReportRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ChatgptService chatgptService;
    private final ObjectMapper objectMapper;

    public String createPrompt(ReportRequestDto request){
        String texiInfo = "";
        try {
            texiInfo = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String systemInfo = getString();

        List<MultiChatMessage> messages = Arrays.asList(
                new MultiChatMessage("system", systemInfo),
                new MultiChatMessage("user", texiInfo)
        );

        return chatgptService.multiChat(messages);
    }

    public String createReport(ReportRequestDto request, String prompt){
        return String.format(
                "탑승 일자 : " + LocalDate.now() + "\n" +
                        "탑승 장소 : " + request.getStart() + "\n" +
                        "도착 장소 : " + request.getEnd() + "\n" +
                        "차량 번호 : " + request.getCarnum() + "\n" +
                        "신고 내용 : 부당요금 신고\n" +
                        "신고자 : “사용자 기입”" + "\n\n" +
                        prompt
        );
    }

    private String getString() {
        return String.format("Based on the information provided by the user in the json format, we would like to write a report text about unfair taxi fares.\n" +
                "\n" +
                "{\"start\": \"군산시 미룡동 365-23\",\"end\" : \"군산시 미룡동 365-23\",\"carnum\":\"12가5468\",\"real\":{\"usetime\":30,\"fee\":5000,\"distance\":50},\"predict\":{\"usetime\":40,\"fee\":10000,\"distance\":100}\n" +
                "\n" +
                "Data in the following json format are given.\n" +
                "Real is information on the actual taxi on board, and user time means boarding time, fee means fare, and distance means travel distance.\n" +
                "Predict is the expected information using the map application, and usetime means the estimated boarding time, fee means the estimated fare, and distance means the estimated travel distance.\n" +
                "\n" +
                "Write that the actual charge was higher than the estimated amount checked by the map application\n" +
                "\n" +
                "Write the last word that you want to report it\n" +
                "\n" +
                "We also provided a taxi number,start_point and end_point, but don't include it in the report\n" +
                "\n" +
                "Write a text message to report unfair taxi fares in Korean based on the information.\n" +
                "\n" +
                "Write it in one paragraph within 300 characters");
    }
}
