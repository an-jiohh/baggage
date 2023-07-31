package com.example.baggage.bungchan.service;

import com.example.baggage.dto.FoodResponseDto;
import com.example.baggage.dto.KaKaoResponseDto;
import org.openqa.selenium.By;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BcCrawlingSevice {

    private WebDriver driver;
    // Properties 설정
    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    // WebDriver 경로
    @Value("${crawling.web_driver_path}")
    public String WEB_DRIVER_PATH;

    private void chrome() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // webDriver 옵션 설정.
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--lang=ko");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.setCapability("ignoreProtectedModeSettings", true);

        // weDriver 생성.
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    private  void turnOffSeleniumDriver(){
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public FoodResponseDto.ShopList urlCrawling(String id , String Category_name){

        //식당이름, 메뉴이름, 메뉴 가격, 평점, 리스트
        FoodResponseDto.ShopList ShopListeDto = new FoodResponseDto.ShopList();
        try {
            String url = "https://place.map.kakao.com/"+id;
            driver.get(url);
            // 페이지 불러오는 여유시간
            driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
            //메뉴
            WebElement menu_list = driver.findElement(By.cssSelector(".list_menu"));
            //메뉴 이름, 메뉴 가격  정보.
            List<WebElement> menu_info = menu_list.findElements(By.cssSelector(".info_menu"));


            try{
                //평점
                WebElement grade = driver.findElement(By.cssSelector(".grade_star > .num_rate"));
                //평점 문자열뒤 (점) 제거
                String score = grade.getText().replace("점", "");
                ShopListeDto.setScore(score);
            }
            catch (NoSuchElementException e) {
                //평점 없을시 0,
                System.out.println(id +"/평점 없음");
                ShopListeDto.setScore("0");
            }

            //리뷰
            //WebElement review_list = driver.findElement(By.cssSelector(".list_evaluation"));
            //List review = review_list.findElements(By.cssSelector(".txt_comment > span"));
            //리스트로 반환 인지만 .get(0).gettext()로 추출하여 리스트 저장 작업 해야함

            for (int i = 0; i < menu_info.size(); i++) {
                try {
                    //메뉴, 가격 둘다 있다면

                    // 문자 앞("추천") 제거
                    String menuname = menu_info.get(i).findElement(By.cssSelector("span.loss_word")).getText().replace("추천\n", "");
                    //가격 String to int 처리
                    int menuprice = Integer.parseInt(menu_info.get(i).findElement(By.cssSelector(".price_menu")).getText().replace(",", ""));
//                    System.out.println("id: "+id +"이름 : " + menuname + "  " + "가격 : " + menuprice);
                    //메뉴, 가격 둘중 하나가 없어도 추가X
                    if(menuname != null && menuprice != 0) {

                        // 카테고리 가 포함된 메뉴 이름만 추가
                        //추후 수정
                        if (menuname.contains(Category_name) || (Category_name.length() >= 3 && menuname.contains(Category_name.substring(1)))) {
                            ShopListeDto.setShopmenu(menuname);
                            ShopListeDto.setShopprice(menuprice);
                            //메뉴 하나만 추출
                            return ShopListeDto;
                            }
                    }
                } catch (NoSuchElementException e) {
//                    System.out.println(id+"[메뉴정보]메뉴 or 가격이 없음");
                    continue;
                } catch (NumberFormatException e)
                {
                    System.out.println(id+"변환 오류");
                    continue;
                }
                return null;
            }



        } catch (NoSuchElementException e) {
//            System.out.println(e);
            System.out.println(id+"경로문제 or 메뉴 없음");
            // 없는 경로 또는, 메뉴 가격이 없는경우
        }
        return null;
    }

    public  FoodResponseDto crawling(KaKaoResponseDto kaKaoResponseDto , String Category_name)
    {
        chrome();

        FoodResponseDto foodResponseDto = new FoodResponseDto();
        //foodResponseDto 에 추가할 List
        List<FoodResponseDto.ShopList> foodshopList = new ArrayList<>();

        List<KaKaoResponseDto.Document> documents = kaKaoResponseDto.getDocuments();

        for(int i = 0 ; i<documents.size();i++)
        {
            FoodResponseDto.ShopList shopList = urlCrawling(documents.get(i).getId(), Category_name);
            if (shopList != null)
            {
                shopList.setShopname(documents.get(i).getPlace_name());
                shopList.setAddress(documents.get(i).getRoad_address_name());
                foodshopList.add(shopList);
            }

        }
        foodResponseDto.setShoplist(foodshopList);

        turnOffSeleniumDriver();
        return foodResponseDto;
    }

    
}
