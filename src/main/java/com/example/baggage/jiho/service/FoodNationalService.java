package com.example.baggage.jiho.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FoodNationalService {

    private Map<String, Integer> nationList = new HashMap<>(); //읽기만 할것으로 HashMap사용

    public int getNationalPrice(String menu){
        for (String s : nationList.keySet()) {
            if(s.contains(menu)){
                return nationList.get(s);
            }
        }
        return 0;
    }

    @PostConstruct
    private void initNationPrice(){
        nationList.put("갈비탕",12640);
        nationList.put("감자탕",25115);
        nationList.put("곰탕",10574);
        nationList.put("김치찌개",8165);
        nationList.put("낙지볶음",15638);
        nationList.put("닭갈비",12497);
        nationList.put("돈가스",9400);
        nationList.put("돼지갈비",16692);
        nationList.put("돼지국밥",8205);
        nationList.put("등심구이",28999);
        nationList.put("떡볶이",6607);
        nationList.put("모듬회",38651);
        nationList.put("목살",14168);
        nationList.put("물냉면",6075);
        nationList.put("백반",12750);
        nationList.put("병맥주(국산)",4410);
        nationList.put("병맥반주(수입)",6698);
        nationList.put("부대찌개",11104);
        nationList.put("불고기/주물럭",	15010);
        nationList.put("비빔밥",8728);
        nationList.put("뼈해장국",8768);
        nationList.put("삼겹살",12058);
        nationList.put("생맥주(국산)",3873);
        nationList.put("생선구이",14467);
        nationList.put("샤브샤브",17778);
        nationList.put("소갈비",23829);
        nationList.put("소곱창구이",18846);
        nationList.put("소주",4233);
        nationList.put("순대국밥",8217);
        nationList.put("스테이크",22457);
        nationList.put("아귀찜",34911);
        nationList.put("아메리카노",3511);
        nationList.put("우동",6817);
        nationList.put("육개장",8977);
        nationList.put("일반김밥",2862);
        nationList.put("장어구이",31259);
        nationList.put("족발",32689);
        nationList.put("짜장면",7799);
        nationList.put("짬뽕",7982);
        nationList.put("쭈꾸미볶음",14902);
        nationList.put("초밥",13198);
        nationList.put("추어탕",9171);
        nationList.put("치킨",13557);
        nationList.put("카페라떼",4235);
        nationList.put("칼국수",7861);
        nationList.put("탄산음료",1943);
        nationList.put("탕수육",15948);
        nationList.put("파스타/스파게티",13619);
        nationList.put("피자",12765);
    }
}
