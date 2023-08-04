package com.example.baggage.jungchan.controller;


import com.example.baggage.jungchan.domain.Review;
import com.example.baggage.jungchan.domain.ReviewRequestBodyDto;
import com.example.baggage.jungchan.domain.ReviewResponseDto;
import com.example.baggage.jungchan.service.gpt.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping("/review/save")
    public ReviewResponseId createReview(@RequestBody ReviewRequestBodyDto reviewRequestBodyDto){
        Review review = new Review();
        review.setReview(reviewRequestBodyDto.getReview());
        review.setRate(reviewRequestBodyDto.getRate());
        review.setRegion(reviewRequestBodyDto.getRegion());

        Long id = reviewService.join(review);

        return new ReviewResponseId(id);
    }

    @GetMapping("/review/{region}")
    public ReviewResponseDto showReview(@PathVariable("region") String region){
        List<Review> findReviewByRegion = reviewService.findByRegion(region);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        List<ReviewResponseDto.ReviewList> reviewList = new ArrayList<>();

        //평균 구해서 넣기
        reviewResponseDto.setAvgrate(reviewService.getAvgRateByRegion(region));

        for(int i=0; i<findReviewByRegion.size(); i++){
            ReviewResponseDto.ReviewList review = new ReviewResponseDto.ReviewList();
            review.setRate(findReviewByRegion.get(i).getRate());
            review.setReview(findReviewByRegion.get(i).getReview());
            reviewList.add(review);
        }

        reviewResponseDto.setReviewlist(reviewList);

        return reviewResponseDto;
    }

    @Data
    @AllArgsConstructor
    static class ReviewResponseId{
        private Long id;

    }
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
