package com.example.baggage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class LodgmentResponsDto {
    private  int code;
    private List<LodgmentResponsDto.LodgmentList> lodgmentlist;


    @Getter
    @Setter
    public static  class  LodgmentList {

        private String add1;
        private String add2;
        private String title;
    }
}
