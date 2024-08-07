package com.shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.DTO.Order;
import com.shop.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrdersByMemberId(int memberId) {
        return orderRepository.findByMemberId(memberId);
    }

    @Transactional
    public boolean updateDeliveryStatus(int orderId, String newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setDeliveryStatus(newStatus);
            orderRepository.save(order);
            return true;
        }
        return false;
    }
}
