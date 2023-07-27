package com.example.baggage.jiho.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceServ {
    private final  String apiUrl = "https://api.openai.com/v1/chat/completions";
    private final  String apiKey = "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";
    private final ObjectMapper objectMapper;

    public String createPrompt(){
        return String.format("안녕하세요"+"입니다.");
    }

    public String createReport(){

        String payload = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"user\", \"content\": \"Who won the world series in 2020?\"}]}";
//        "{"model": "gpt-3.5-turbo",
//          "messages": [
//              {"role": "system",
//               "content": "너는 머를 잘할"
//               },
//               {"role": "user",
//               "content": "내가 얼맘가나왔는데 바가지야??"
//               }
//          ]
//         }";
        WebClient client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String report = client.post().body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info(report);
        try{
            JsonNode jsonNode = objectMapper.readTree(report);
            JsonNode messageNode = jsonNode.at("/choices/0/message/content");
            String assistantResponse = messageNode.asText();

            return assistantResponse;
        } catch (JsonMappingException e) {
            return null;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
