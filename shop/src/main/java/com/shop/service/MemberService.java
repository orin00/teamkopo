package com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.DTO.Member;
import com.shop.repository.MemberRepository;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public void deleteMemberById(int memberId) {
        memberRepository.deleteById(memberId);
    }
    

    public boolean isAdmin(int id) {
        Member member = memberRepository.findById(id).orElse(null);
        return member != null && "manager".equals(member.getRole());
    }
}
