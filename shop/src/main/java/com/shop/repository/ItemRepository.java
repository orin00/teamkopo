package com.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.DTO.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{
	
	// 여러 개의 아이템 ID에 해당하는 아이템들 조회
	List<Item> findByIdIn(List<Integer> ids);
	
	// 특정 카테고리에 해당하는 모든 상품 조회
	List<Item> findByCategory(String category);
    
    // 단일 아이템 ID에 해당하는 아이템 조회
    Optional<Item> findById(Integer id);
}
