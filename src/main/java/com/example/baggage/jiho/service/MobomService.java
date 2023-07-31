package com.example.baggage.jiho.service;

import com.example.baggage.domain.Mobom;
import com.example.baggage.dto.FoodResponseDto;
import com.example.baggage.jiho.repository.MobomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MobomService {

    private final MobomRepository mobomRepository;

    public List<FoodResponseDto.MobomList> getMobomData(String address, String menu){
        List<Mobom> mobomList = mobomRepository.findByADDRESSContainsAndHEADMENUContains(address, menu);
        if(mobomList.size()==0){ //값이 없으면 모든것으로 검색
            mobomList = mobomRepository.findByADDRESSContainsAndHEADMENUContains(address, "");
        }
        List<FoodResponseDto.MobomList> returnLists = new ArrayList<>(10);
        int count = 0;
        for (Mobom mobom : mobomList) {
            if (count++ == 5) break;
            FoodResponseDto.MobomList mobomshop = new FoodResponseDto.MobomList();
            mobomshop.setAddress(mobom.getADDRESS());
            mobomshop.setHeadmenu(mobom.getHEADMENU());
            mobomshop.setShopname(mobom.getSHOPNAME());
            returnLists.add(mobomshop);
        }
        return returnLists;
    }
}
