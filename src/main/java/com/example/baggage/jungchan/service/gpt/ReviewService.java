package com.example.baggage.jungchan.service.gpt;

import com.example.baggage.jungchan.domain.Review;
import com.example.baggage.jungchan.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;


    @Transactional
    public Long join(Review review){
        reviewRepository.save(review);
        return review.getId();
    }

    public List<Review> findReview(){
        return reviewRepository.findAll();
    }
    public List<Review> findByRegion(String region){
        return reviewRepository.findByRegion(region);
    }

    public double getAvgRateByRegion(String region){
        return reviewRepository.getAvg(region);
    }
}
