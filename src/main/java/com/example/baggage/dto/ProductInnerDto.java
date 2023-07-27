package com.example.baggage.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductInnerDto {
    private int userprice;
    private String productName;
    private int price;
    private int weekprice;
    private int monthprice;

    private int nationalprice;

}
