package com.cloud.assignment.entity; // 이 경로와 폴더 구조가 같아야 합니다.

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member", catalog = "assignment")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String mbti;

    @Column(name = "profile_image_name")
    private String profileImageName;
}