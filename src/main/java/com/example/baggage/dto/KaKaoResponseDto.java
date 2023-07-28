package com.example.baggage.dto;

import lombok.Data;

import java.util.List;

@Data
public class KaKaoResponseDto {
    private List<Document> documents;
    @Data
    public static class Document{

        private String id;
        private String place_name;
        private String road_address_name;

    }

}
