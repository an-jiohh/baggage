package com.example.baggage.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductsRequestDto {
    private String p_itemcategorycode;

    private String p_itemcode;
    private String p_kindcode;
    private String p_countycode;
    private String userprice;
}
