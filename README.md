# 🚀 AWS 클라우드 배포 프로젝트

Spring Boot 애플리케이션을 AWS EC2와 RDS(MySQL), S3를 활용하여 배포하고 기능을 확장한 프로젝트입니다.

---

## 🌟 LV 0 - AWS Budget 설정 (필수)

> 💡 클라우드 실습 중 가장 중요한 것은 비용 관리입니다. 실수로 고가의 리소스를 켜두는 것을 방지하기 위해 예산 알림을 설정합니다.

- **설정**
  - 월 예산: $100
  - 예산 80% 도달 시 이메일 알림

**제출용 캡처**
<img width="1271" height="611" alt="AWS Budget 캡처" src="https://github.com/user-attachments/assets/ea8696d0-fc17-4fa6-9754-6111ca8dbed0" />

---

## 🌐 LV 1 - 네트워크 구축 및 애플리케이션 배포 (필수)

> 안전한 네트워크 환경을 구축하고 운영 가능한 상태의 애플리케이션을 배포합니다.

### 1. 인프라 구축
- **VPC 설계**: Public / Private Subnet 분리
- **EC2 생성**: Public Subnet에 EC2 배포
- **퍼블릭 IP 확인**
  - EC2 퍼블릭 IP: `3.35.140.3`

### 2. 애플리케이션 기능
- **API 구현**
  - `POST /api/members`: 팀원 이름, 나이, MBTI 정보를 JSON으로 저장
  - `GET /api/members/{id}`: 저장된 팀원 정보를 조회
- **Profile 분리**
  - 로컬: H2 DB 사용
  - 운영: MySQL 사용
- **로그 전략**
  - API 요청 시 `INFO` 레벨로 "[API - LOG] 요청 정보" 출력
  - 예외 발생 시 `ERROR` 레벨로 스택트레이스 출력
- **Actuator Health 체크**
  - `/actuator/health` 엔드포인트 노출
  - 상태 확인: [http://3.35.140.3:8080/actuator/health](http://3.35.140.3:8080/actuator/health)

**[Health Check 결과]**
<img width="777" alt="Health Check" src="https://github.com/user-attachments/assets/a5794ec9-6f10-4aa8-8531-6398956ec04e" />

---

## ✅ LV 2 - DB 분리 및 파라미터 관리 (필수)

AWS 관리형 서비스를 활용하여 데이터베이스를 분리하고 민감 정보를 안전하게 관리하도록 구성했습니다.

### 1. 인프라 및 보안 설정
- **RDS 구축**: AWS RDS(MySQL 8.0)를 분리된 환경에 생성하여 데이터 독립성 확보
- **보안 그룹 체이닝**: RDS 보안 그룹의 인바운드 소스를 **EC2의 보안 그룹 ID**로 설정, 0.0.0.0/0 개방 없이 EC2 ↔ RDS 간 보안 연결만 허용
<img width="1258" alt="RDS 보안 그룹" src="https://github.com/user-attachments/assets/812c7865-eadf-4815-ac98-40c2238d8d91" />

### 2. AWS Parameter Store 연동
- **민감 정보 관리**: DB URL, Username, Password 등을 SSM Parameter Store에 저장하여 코드 내 노출 방지
- **Actuator Info 확장**: `/actuator/info` 호출 시 Parameter Store의 `team-name` 값을 출력
- **상태 확인 엔드포인트**: [http://3.35.140.3:8080/actuator/info](http://3.35.140.3:8080/actuator/info)

<img width="1992" alt="Actuator Info" src="https://github.com/user-attachments/assets/620821f2-115a-46be-aabe-57fffaf64150" />

---

## ✅ LV 3 - S3 이미지 업로드 및 보안 강화 (필수)

객체 스토리지인 S3를 연동하여 파일 저장 기능을 구현하고, 보안 유지를 위해 Presigned URL 방식을 도입했습니다.

### 1. S3 이미지 업로드 구현
- **보안 접근 (Presigned URL)**: S3 버킷을 퍼블릭으로 개방하지 않고, **7일 유효기간**의 임시 URL 발급
- **용량 최적화**: `spring.servlet.multipart.max-file-size`를 10MB로 확장
- **환경 변수 보안**: 로컬 테스트 시에도 비밀번호 유출 방지 (`${DB_PASSWORD}`)

### 2. 🖼️ 과제 제출용 이미지 확인 (Presigned URL)
> **주의**: 아래 URL은 발급일로부터 7일간(2026-02-09까지)만 유효합니다.

- **발급된 URL**: [이미지 확인하기(클릭)](https://my-member-profile-bucket.s3.ap-northeast-2.amazonaws.com/profile/1_%EA%B9%80%EA%B7%9C%EB%B2%94%EC%82%AC%EC%A7%84.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20260201T215045Z&X-Amz-SignedHeaders=host&X-Amz-Credential=AKIA2NVKOTH2PMOEBUXE%2F20260201%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Expires=604800&X-Amz-Signature=3edd684f5570e7ffdeb51fca5dd0722626ab5345d8ce3476bfacfeed1981108f)
- **만료 일시**: 2026-02-09 07:00 (KST)

**[업로드 및 조회 검증]**
<img width="2559" alt="S3 Result" src="https://github.com/user-attachments/assets/cb30dbe6-2582-42bd-bb35-98a067efdb34" />

---

## 🛠️ 기술 스택
- **Language**: Java 17
- **Framework**: Spring Boot 3.3.2
- **Database**: AWS RDS (MySQL 8.0)
- **Storage**: AWS S3
- **Infrastructure**: AWS EC2 (Amazon Linux 2023), SSM Parameter Store

---

## 📝 트러블슈팅
- **이슈**: 3MB 이상의 이미지 업로드 시 `413 Request Entity Too Large`
  - **해결**: `application.yml`에 `multipart` 용량 제한 설정
- **이슈**: EC2 세션 종료 시 서버 프로세스 중단
  - **해결**: `nohup` 명령어로 백그라운드 상주
- **이슈**: RDS 보안 위협 가능성
  - **해결**: EC2 보안 그룹 ID를 참조하는 **보안 그룹 체이닝** 적용

---
※ 실행 가이드: 로컬 환경에서 실행 시, IDE(IntelliJ 등)의 Environment Variables 설정에 DB_PASSWORD를 본인의 로컬 DB 비밀번호로 설정해야 정상 동작합니다.
