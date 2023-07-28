package com.example.baggage.jungchan.service.gpt;

import com.example.baggage.dto.FoodResponseDto;
import com.example.baggage.dto.ProductInnerDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class GptService {

    public String createTaxiPrompt(int realDistance, int realFee, int realUsetime, int predictDistance, int predictFee, int predictUsetime ) {
        String prompt = String.format(
                "택시비 바가지는 택시요금을 원래의 가격보다 부당하게 높게 받는것을 택시요금 바가지라고 해.[output.]\n" +
                        "예상 요금: %d 원, 예상 거리는 %d km, 예상 시간은 %d 분이였어. 실제 요금: %d 원, 거리는 %d km, 예상 시간은 %d 분이 걸렸어. [output.]\n" +
                        "택시비 바가지 우선순위 기준은" +
                        "1.실제요금이 예상 요금보다 30%% 이상 높은지\n " +
                        "2.실제거리가 예상 거리보다 30%% 이상 많은지\n" +
                        "3.실제시간이 예상 시간보다 30%% 이상 많은지\n" + "를 기준으로 둘게[output.]\n" +
                        "이 데이터에 대해 분석해주고, 답변 형식은 최대한 정중한 말투로 중립을 지키면서 기준에 의해 판단해서 답변을 해줘, 그리고 실제와 예상이 얼마나 차이나는지 알려줘." +
                        "또 너가 판단 했을 떄 택시비 바가지의 위험정도를 적정,의심,위험,매우위험 정도로 판단해줘(내가 라는 말 사용 금지, 제가로 대체)"
                ,predictFee, predictDistance , predictUsetime , realFee , realDistance , realUsetime );

        return prompt;
    }

    public String createProductPrompt(ProductInnerDto productInnerDto){
        String prompt = String.format(
                "농수산물 바가지는 가격을 원래의 가격보다 부당하게 높게 받는것을 농수산물 바가지라고 해.[output.]\n" +
                        "오늘 평균 가격: %d 원, 지난주 평균 가격 %d 원, 지난달 평균 가격 %d 원, 전국 평균 가격은 %d원 이였어. 실제 구매 가격: %d 원 [output.]\n" +
                        "농수산물 바가지의 기준은" +
                        "1.실제 구매 가격이 오늘 평균 가격보다 15%% 이상 높다면 바가지이다.\n " +
                        "2.실제 구매 가격이 (지난달 평균, 지난주 평균, 오늘 평균)의 평균가격 보다 20%% 이상 많다면 바가지이다.\n" +
                        "3.실제 구매 가격이 전국 평균 가격 보다 15%% 이상 높다면 바가지이다.\n" +
                        "를 기준으로 둘게. 실제 구매 가격이 평균가격보다 낮다면 바가지가 아니야. [output.]\n" +
                        "이 데이터에 대해 분석해주고, 답변 형식은 최대한 정중한 말투로 중립을 지키면서 기준에 의해 판단해서 답변을 해줘, 그리고 실제와 평균 가격들이 얼마나 차이나는지 알려줘." +
                        "또 너가 판단 했을 떄 농수산물 바가지의 위험정도를 저렴,적정,의심,위험,매우위험 정도로 판단해줘(내가 라는 말 사용 금지, 제가로 대체)",
                productInnerDto.getPrice(), productInnerDto.getWeekprice(), productInnerDto.getMonthprice(), productInnerDto.getNationalprice(), productInnerDto.getUserprice());
        return prompt;
    }

    public String createFoodPrompt(FoodResponseDto foodResponseDto, int realPrice){
        String prompt = String.format(
                "음식 바가지는 음식의 가격을 원래의 가격보다 부당하게 높게 받는 것을 음식 바가지라해.[output.]\n" +
                        "음식의 전국 평균 가격 = %d, 지역 평균 가격 = %d, 지역 최소 가격 = %d, 지역 최대 가격 = %d, 실제 가격야 = %d." +
                        "너가 위의 데이터를 종합해서 음식의 가격이 바가지인지 아닌지 판단해줘." +
                        "만약에 전국 평균 가격의 정보가 없는 경우 배제하고 판단해줘,[output.]\n" +
                        "답변 형식은 최대한 정중한 말투로 중립을 지키면서 기준에 의해 판단해서 답변을 해줘, 그리고 실제 가격과 (지역,전국)평균 가격들이 얼마나 차이나는지 알려줘." +
                        "또 너가 판단 했을 떄 음식 바가지의 위험정도를 저렴,적정,의심,위험,매우위험 정도로 판단해줘(내가 라는 말 사용 금지, 제가로 대체)"
                , foodResponseDto.getNationalprice(), foodResponseDto.getAvgprice(), foodResponseDto.getMinprice(), foodResponseDto.getMaxprice(), realPrice
        );
        return prompt;
    }
}