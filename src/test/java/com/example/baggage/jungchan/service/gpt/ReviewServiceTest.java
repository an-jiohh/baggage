package com.example.baggage.jungchan.service.gpt;

import com.example.baggage.jungchan.domain.Review;
import com.example.baggage.jungchan.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@RunWith(SpringRunner.class)
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;


    @Test
    @Transactional
    void join() {
        Review review = new Review();
//        review.setId(212L);
        review.setReview("asd");
        review.setRate(3.5);
        review.setRegion("kusan");


        reviewService.join(review);


    }

    @Test
    @Transactional
    void 전체리뷰조회() {

        Review review = new Review();
        review.setReview("asd");
        review.setRate(3.5);
        review.setRegion("kusan");

        reviewService.join(review);

        System.out.println(reviewService.findReview().get(0).toString());
    }

    @Test
    @Transactional
    void 지역리뷰조회() {
        reviewService.findByRegion("kusan");


    }

    @Test
    @Transactional
    void 평균값(){
        for(int i=0; i <10; i++){

            Review review = new Review();
            review.setReview("asd");
            review.setRate(i);
            review.setRegion("kunsan");
            reviewRepository.save(review);
        }


        System.out.println(reviewRepository.getAvg("kunsan"));
    }
}