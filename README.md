# traveler-backend
## ๐ป clean commit
- ์ ๋ชฉ์ ๊ธธ์ด๋ ์ต๋ 48๊ธ์๊น์ง ํ๊ธ๋ก ๊ฐ๋จ ๋ช๋ฃํ๊ฒ ์์ฑ
- ์ ๋ชฉ์ ์์ฑํ๊ณ  ๋ฐ๋์ ๋น ์ค ํ ์ค์ ๋ง๋ค์ด์ผ ํจ.
- ์ ๋ชฉ์ .(๋ง์นจํ) ๊ธ์ง

- label ๋ฆฌ์คํธ
  - feat : ์๋ก์ด ๊ธฐ๋ฅ
  - bug : ๋ฒ๊ทธ ์์ 
  - update : ๋น์ฆ๋์ค ๋ก์ง ๋ณ๊ฒฝ
  - docs : ๋ฌธ์(๋ฌธ์ ์ถ๊ฐ, ์์ , ์ญ์ )
  - test : ํ์คํธ
  - etc : ๊ธฐํ ๋ณ๊ฒฝ์ฌํญ
  
## description

๋ด์ฉ์ ๊ธธ์ด๋ ํ์ค๋น 60๊ธ์ ๋ด์ธ์์ ์ค ๋ฐ๊ฟ. ํ๊ธ๋ก ๊ฐ๋จ ๋ช๋ฃํ๊ฒ ์์ฑ

์ด๋ป๊ฒ ๋ณด๋ค๋ ๋ฌด์์, ์ ๋ณ๊ฒฝํ๋์ง๋ฅผ ์์ฑํ  ๊ฒ(ํ์)

## issue-number

์ฐ๊ด๋ ์ด์ ์ฒจ๋ถ, ์ฌ๋ฌ ๊ฐ ์ถ๊ฐ ๊ธฐ๋ฅ



# ๊ฐ๋ฐํ๊ฒฝ ์๋ฒ ๊ตฌ๋๋ฒ

## ์ฌ์  ์ค๋น์ฌํญ
1. docker desktop ์ค์น

## mysql ๊ตฌ๋
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

## ์ด๊ธฐ์ mysql ํ์ด๋ธ ์์ฑ๋๋๋ก ์ค์  ๋ณ๊ฒฝ
traveler-backend/src/main/resources/application.yml
```yml
  jpa:
    open-in-view: true
    hibernate:
      # update์์ create๋ก ์์ 
      ddl-auto: create
```

## redis ๊ตฌ๋
```bash
docker run -d -p 6379:6379 --name redis-test \
redis:7.0.5
```

## intellij๋ฅผ ์ฌ์ฉํ spring ์คํ
control + r
