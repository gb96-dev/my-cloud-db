package com.cloud.assignment.controller;

import com.cloud.assignment.entity.Member;
import com.cloud.assignment.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 1. 로그를 위해 추가
import org.springframework.web.bind.annotation.*;

@Slf4j // 2. 로그를 위해 추가
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public Member createMember(@RequestBody Member member) {
        // 3. 요구사항에 맞는 로그 남기기
        log.info("[API - LOG] 팀원 저장 요청: {}", member.getName());
        return memberService.save(member);
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable Long id) {
        // 4. 요구사항에 맞는 로그 남기기
        log.info("[API - LOG] 팀원 조회 요청 ID: {}", id);
        return memberService.findById(id);
    }
}