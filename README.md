## "HELLO, ITWC Backend 🥇️"

<BR><BR>



---

## Local Command

로컬에서 테스트용으로 사용하는 스크립트

<br>

### Run App With Infra

서버와 함께 사용되는 인프라 구성을 함께 올린다. (ex MySQL, Redis...)

소스코드 수정 O

````
docker-compose up --build
````

소스코드 수정 X

````
docker-compose up
````

<br><br><br>

### Run App Without Infra

소스코드 수정 O

````
docker build --build-arg SPRING_PROFILES_ACTIVE=local -t itwc-back . && 
docker run -v ./logs:/logs -p 8080:8080 itwc-back
````

소스코드 수정 X

````
docker run -v ./logs:/logs -p 8080:8080 itwc-back
````

<br><br><br>

### Swagger

````
http://localhost:8080/swagger-ui/index.html
````

<br><br><br>

### H2-console

````
http://localhost:8080/h2-console
````

---

### 월드컵 게임 서버 Docker run

docker build --build-arg SPRING_PROFILES_ACTIVE=local -f itwc-world-cup.Dockerfile -t itwc-world-cup . &&
docker run -v ./logs:/logs -p 8080:8080 itwc-world-cup

docker build --build-arg SPRING_PROFILES_ACTIVE=local -f itwc-member.Dockerfile -t itwc-member . &&
docker run -v ./logs:/logs -p 8081:8081 itwc-member
