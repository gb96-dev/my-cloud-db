package com.cloud.assignment.controller;

import com.cloud.assignment.entity.Member;
import com.cloud.assignment.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public Member createMember(@RequestBody Member member) {
        log.info("[API] 팀원 저장: {}", member.getName());
        return memberService.save(member);
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long id,
                                                     @RequestParam("file") MultipartFile file) throws IOException {
        memberService.updateProfileImage(id, file);
        return ResponseEntity.ok("이미지 업로드 성공!");
    }

    @GetMapping("/{id}/profile-image")
    public ResponseEntity<String> getProfileImage(@PathVariable Long id) {
        String presignedUrl = memberService.getPresignedUrl(id);
        return ResponseEntity.ok(presignedUrl);
    }
}