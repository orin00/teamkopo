package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.DTO.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByMemberIdOrderByIdDesc(int memberId);
    
    List<Order> findByMemberId(int memberId);
}