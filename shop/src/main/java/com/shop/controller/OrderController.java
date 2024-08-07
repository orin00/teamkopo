package com.shop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.shop.DTO.Order;
import com.shop.repository.CartRepository;
import com.shop.repository.OrderRepository;
import com.shop.service.JwtService;
import com.shop.service.OrderService;

import jakarta.transaction.Transactional;

@RestController
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    CartRepository cartRepository;

    @Autowired
    JwtService jwtService;

    @GetMapping("/api/orders")
    public ResponseEntity getOrder(
            @CookieValue(value = "token", required = false) String token
    ) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        List<Order> orders = orderRepository.findByMemberIdOrderByIdDesc(memberId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/api/orders")
    public ResponseEntity pushOrder(
            @RequestBody Order orderOptional,
            @CookieValue(value = "token", required = false) String token
    ) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Order newOrder = new Order();

        newOrder.setMemberId(memberId);
        newOrder.setName(orderOptional.getName());
        newOrder.setAddress(orderOptional.getAddress());
        newOrder.setAddressDetail(orderOptional.getAddressDetail());
        newOrder.setPayment(orderOptional.getPayment());
        newOrder.setCardNumber(orderOptional.getCardNumber());
        newOrder.setItems(orderOptional.getItems());
        newOrder.setDeliveryStatus(orderOptional.getDeliveryStatus());
        newOrder.setCreatedAt(orderOptional.getCreatedAt());

        orderRepository.save(newOrder);
        cartRepository.deleteByMemberId(memberId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/api/orders/{memberId}")
    public ResponseEntity<List<Order>> getOrdersByMemberId(@PathVariable int memberId) {
        List<Order> orders = orderService.getOrdersByMemberId(memberId);
        return ResponseEntity.ok().body(orders);
    }

    @PutMapping("/api/orders/{orderId}/update-delivery-status")
    public ResponseEntity<String> updateDeliveryStatus(
            @PathVariable int orderId,
            @RequestParam String status
    ) {
        boolean updated = orderService.updateDeliveryStatus(orderId, status);
        if (updated) {
            return ResponseEntity.ok().body("Delivery status updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
    }
}