package com.example.baggage.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class FoodResponseDto {
    private int avgprice;
    private int maxprice;
    private int minprice;
    private int nationalprice;
    private String prompt;

    private List<ShopList> shoplist;

    private List<ShopList> mobomlist;
    @Getter
    @Setter
    private static class ShopList {
        private String shopname;
        private String shopmenu;
        private int shopprice;
        private String score;
    }

    @Getter
    @Setter
    private static class mobomlist {
        private String shopname;
        private String adress;
        private String headmenu;
    }

}
