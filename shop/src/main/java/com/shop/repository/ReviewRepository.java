package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.DTO.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	 List<Review> findByItemId(Integer itemId);
}
