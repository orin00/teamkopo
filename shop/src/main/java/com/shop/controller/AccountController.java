package com.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.shop.DTO.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private JwtService jwtService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> params, HttpServletResponse res) {
        Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));
        
        if (member != null) {
            int id = member.getId();
            String nickname = member.getNickname();
            String role = member.getRole();
            
            String token = jwtService.getToken("id", id);
            
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            res.addCookie(cookie);

            Map<String, Object> response = new HashMap<>();
            response.put("id", id);
            response.put("nickname", nickname);
            response.put("role", role);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        res.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 로그인을 했는지 안했는지 체크
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> check(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtService.getClaims(token);
        
        if (claims != null) {
            int id = Integer.parseInt(claims.get("id").toString());
            Member member = memberRepository.findById(id).orElse(null);
            
            if (member != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("id", id);
                response.put("nickname", member.getNickname());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
    // 관리자(manager) 권한 있는지 없는지를 체크
    @GetMapping("/admin/check")
    public ResponseEntity<Boolean> isAdmin(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtService.getClaims(token);

        if (claims != null) {
            int id = Integer.parseInt(claims.get("id").toString());
            Member member = memberRepository.findById(id).orElse(null);

            if (member != null && "manager".equals(member.getRole())) {
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }
}
