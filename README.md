# traveler-backend
## 💻 clean commit
- 제목의 길이는 최대 48글자까지 한글로 간단 명료하게 작성
- 제목을 작성하고 반드시 빈 줄 한 줄을 만들어야 함.
- 제목에 .(마침표) 금지

- label 리스트
  - feat : 새로운 기능
  - bug : 버그 수정
  - update : 비즈니스 로직 변경
  - docs : 문서(문서 추가, 수정, 삭제)
  - test : 테스트
  - etc : 기타 변경사항
  
## description

내용의 길이는 한줄당 60글자 내외에서 줄 바꿈. 한글로 간단 명료하게 작성

어떻게 보다는 무엇을, 왜 변경했는지를 작성할 것(필수)

## issue-number

연관된 이슈 첨부, 여러 개 추가 기능



# 개발환경 서버 구동법

## 사전 준비사항
1. docker desktop 설치

## mysql 구동
```bash
docker run -d -p 3306:3306 --name mysql-test \
-e MYSQL_ROOT_PASSWORD=root \
-e MYSQL_USER=cos \
-e MYSQL_PASSWORD=19970604 \
-e MYSQL_DATABASE=travel \
-v /Users/jopopscript/mysql-test-dir:/var/lib/mysql \
mysql:8.0.30
```
https://hub.docker.com/_/mysql

## 초기에 mysql 테이블 생성되도록 설정 변경
traveler-backend/src/main/resources/application.yml
```yml
  jpa:
    open-in-view: true
    hibernate:
      # update에서 create로 수정
      ddl-auto: create
```

## redis 구동
```bash
docker run -d -p 6379:6379 --name redis-test \
redis:7.0.5
```

## intellij를 사용한 spring 실행
control + r
