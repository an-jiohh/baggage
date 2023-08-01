package com.example.baggage.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class FoodResponseDto {
    private int avgprice;
    private int maxprice;
    private int minprice;
    private int nationalprice;
    private String prompt;
    private String rank;

    private List<ShopList> shoplist;

    private List<MobomList> mobomlist;

    @Getter
    @Setter
    @ToString
    public static class ShopList implements Comparable<ShopList>{
        private String shopname;
        private String shopmenu;
        private int shopprice;
        private String score;
        private String address;

        @Override
        public int compareTo(ShopList o) {
            int compareResult = this.getScore().compareTo(o.getScore());
            if (compareResult < 0) {
                return 1;
            } else if (compareResult > 0) {
                return -1;
            }
            return 0;
        }


    }



    @Getter
    @Setter
    public static class MobomList {
        private String shopname;
        private String address;
        private String headmenu;
    }

}
