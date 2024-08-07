package com.shop.controller;
import com.shop.DTO.Review;
import com.shop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/items")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    // 작성된 리뷰를 데이터베이스에 저장    
    @PostMapping("/{itemId}/reviews")
    public ResponseEntity<Review> submitReview(@PathVariable Integer itemId, @RequestBody Review review) {
        review.setItemId(itemId);
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }

    // 특정 상품의 리뷰 전체 조회
    @GetMapping("/{itemId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByItemId(@PathVariable Integer itemId) {
        List<Review> reviews = reviewService.getReviewsByItemId(itemId);
        return ResponseEntity.ok(reviews);
    }
    
    // 리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        boolean deleted = reviewService.deleteReview(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
