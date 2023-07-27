package com.example.baggage.jiho.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ItemnameDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        if (jsonParser.isExpectedStartArrayToken()) {
            jsonParser.nextToken(); // 배열의 시작 토큰인 '['를 스킵합니다.
            if (jsonParser.currentToken() == JsonToken.END_ARRAY) {
                return ""; // 빈 배열인 경우 빈 문자열로 처리합니다.
            }
            jsonParser.nextToken(); // 배열의 첫 번째 요소로 이동합니다.
        }
        return jsonParser.getValueAsString();
    }
}
