package com.shop.controller;

import java.util.List;

import com.shop.DTO.Cart;
import com.shop.DTO.Item;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CartController {
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@GetMapping("/api/cart/items")
	public ResponseEntity<List<Item>> getCartItems(@CookieValue(value = "token", required = false) String token) {

		// 사용자의 토큰이 유효한지 확인
	    if (!jwtService.isValid(token)) {
	    	
	    	// 유효하지 않은 경우 UNAUTHORIZED(401) 응답을 반환
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    // 사용자의 ID를 추출
	    int memberId = jwtService.getId(token);
	    
	    // 사용자의 장바구니 항목을 데이터베이스에서 조회
	    List<Cart> carts = cartRepository.findByMemberId(memberId);
	    
	    // 장바구니에 담긴 각 항목의 아이템 ID를 추출
	    List<Integer> itemIds = carts.stream().map(Cart::getItemId).toList();
	    
	    // 아이템 ID를 기반으로 해당 아이템들의 상세 정보를 조회
	    List<Item> items = itemRepository.findByIdIn(itemIds);

	    // 조회된 상세 정보를 응답으로 반환
	    return new ResponseEntity<>(items, HttpStatus.OK);
	}
	
	@GetMapping("/api/cart/items/{itemId}/quantity")
	public ResponseEntity<Integer> getItemQuantity(
	        @PathVariable("itemId") int itemId,
	        @CookieValue(value = "token", required = false) String token) {

	    if (!jwtService.isValid(token)) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    int memberId = jwtService.getId(token);
	    Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

	    if (cart == null) {
	        return new ResponseEntity<>(0, HttpStatus.OK);
	    }

	    return new ResponseEntity<>(cart.getQuantity(), HttpStatus.OK);
	}
	
	@PostMapping("/api/cart/items/{itemId}")
    public ResponseEntity pushCartItem(
            @PathVariable("itemId") int itemId,
            @RequestParam("quantity") int quantity,
            @CookieValue(value = "token", required = false) String token
    ) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            newCart.setQuantity(quantity); // 수량 설정
            cartRepository.save(newCart);
        } else {
            cart.setQuantity(cart.getQuantity() + quantity); // 기존 수량에 더하기
            cartRepository.save(cart);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	
	@DeleteMapping("/api/cart/items/{itemId}")
    public ResponseEntity removeCartItem(
            @PathVariable("itemId") int itemId,
            @CookieValue(value = "token", required = false) String token
    ) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

        cartRepository.delete(cart);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
