package com.example.baggage.temp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCompareServiceTest {

    @Test
    void compareRank() {

        int avgPrice = 10000;
        int userPrice = 8000;

        double priceGap = userPrice - avgPrice;
        double priceGapPercentage = (priceGap / avgPrice) * 100;

        System.out.println(priceGapPercentage);

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

        System.out.println(rank);
    }
}