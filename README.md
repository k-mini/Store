

# K Store

간단한 중고장터 개발 프로젝트

# 목차
- [개발 환경](#개발-환경)
- [사용 기술](#사용-기술)
- [요구사항](#요구사항)
- [ER 다이어그램](#ER-다이어그램)

## 개발 환경

## 사용 기술

### 프론트엔드

- Html/Css
- Javascript, JQuery
- Thymeleaf

### 백엔드

- Java 11
- SpringBoot 2.7.17
- Spring Data JPA
- QueryDsl

### build tool

- gradle

### 버전 관리

- git, github

## 요구사항

- 로그인, 회원가입
- 회원이 판매하고자하는 아이템을 사진과 함께 올릴 수 있다.
- 댓글, 기능
- 관리자 계정이 따로 존재하며 해당 계정을 통해 회원의 게시글을 삭제할 수 있다.

## 구현 기능

- 로그인, 회원가입
- 

## API 설계

### /auth/** &nbsp;&nbsp;(인증 관련 API)

- /auth/signup (GET)

> 회원가입
> 
> 회원가입 화면을 표시

- /auth/signup (POST)

> 회원가입 처리
> 
> 회원가입 과정을 진행

- /auth/signin (GET)

> 로그인 화면
> 
> 로그인 화면을 표시


### /boards/community/** &nbsp;&nbsp;(거래 게시판 관련)


- /boards/community (GET)
 
> 거래 게시판 목록을 보여주는 화면을 표시
>
> page : 페이지 번호. 0부터 시작한다. (쿼리파라미터)

- /boards/community/{boardId} (GET)

> 해당 게시물을 보여주는 화면을 표시
>
> boardId : 게시물번호

- /boards/community/form  (GET)

> 거래 게시판 작성 화면을 표시

- /boards/community/form (POST) 

> 거래 게시판 작성 처리

- /boards/


## ER 다이어그램


## 아키텍처


