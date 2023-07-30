package com.example.baggage.dto;

import com.example.baggage.jiho.config.ItemnameDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductsDto {
    private List<Object> condition;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private String error_code;
        private List<Item> item;
    }


    @Getter
    @Setter
    @ToString
    public static class Item {
        private String countyname;
        @JsonDeserialize(using = ItemnameDeserializer.class)
        private String itemname;
        @JsonDeserialize(using = ItemnameDeserializer.class)
        private String kindname;
        private String unit;
        private String price;
        private String weekprice;
        private String monthprice;
        private String yearprice;
    }

}
