package com.example.baggage.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductsResponseDto {
    private String prompt;
    private String rank;
    private int price;
    private int weekprice;
    private int userprice;
    private int monthprice;
    private int nationalprice;

    public ProductsResponseDto(String prompt, String rank, int price, int weekprice, int userprice, int monthprice, int nationalprice) {
        this.prompt = prompt;
        this.rank = rank;
        this.price = price;
        this.weekprice = weekprice;
        this.userprice = userprice;
        this.monthprice = monthprice;
        this.nationalprice = nationalprice;
    }
}
