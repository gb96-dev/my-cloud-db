package com.cloud.assignment.service;

import com.cloud.assignment.entity.Member; // 위에서 만든 엔티티를 정확히 임포트
import com.cloud.assignment.repository.MemberRepository;
import io.awspring.cloud.s3.S3Template;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final S3Template s3Template;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public void updateProfileImage(Long id, MultipartFile file) throws IOException {
        String fileName = "profile/" + id + "_" + file.getOriginalFilename();
        s3Template.upload(bucket, fileName, file.getInputStream());

        Member member = findById(id);
        member.setProfileImageName(fileName);
    }

    public String getPresignedUrl(Long id) {
        Member member = findById(id);
        String fileName = member.getProfileImageName();
        if (fileName == null) throw new RuntimeException("파일 없음");

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(7))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    public Member save(Member member) { return memberRepository.save(member); }
    public Member findById(Long id) { return memberRepository.findById(id).orElseThrow(); }
}