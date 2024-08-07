package com.shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.DTO.Review;
import com.shop.repository.ReviewRepository;

@Service
public class ReviewService {
	@Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
    
    public List<Review> getReviewsByItemId(Integer itemId) {
        return reviewRepository.findByItemId(itemId);
    }
    
    public boolean deleteReview(Integer id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            reviewRepository.delete(optionalReview.get());
            return true;
        }
        return false;
    }
}
