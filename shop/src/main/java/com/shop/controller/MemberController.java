package com.shop.controller;

import com.shop.DTO.Member;
import com.shop.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 회원 가입 기능
    @PostMapping("/signup")
    public ResponseEntity<?> createMember(@RequestBody Member member) {
        try {
        	// role이 비어있다면 기본값으로 user를 설정
            if (member.getRole() == null || member.getRole().isEmpty()) {
                member.setRole("user");
            }
            Member savedMember = memberService.saveMember(member);
            return ResponseEntity.ok(savedMember);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while processing your request: " + e.getMessage());
        }
    }

    // 회원 탈퇴 기능
    @DeleteMapping("/{id}")
    public ResponseEntity<?> withdrawMember(@PathVariable int id) {
        try {
            memberService.deleteMemberById(id);
            return ResponseEntity.ok("Member with ID " + id + " has been successfully withdrawn.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while processing your request: " + e.getMessage());
        }
    }
}
