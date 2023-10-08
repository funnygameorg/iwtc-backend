## "HELLO, ITWC Backend 🥇️" 

안내 사항
1. Docker Container 종료 시 로그 파일이 프로젝트 "/logs" 경로에 바인딩 됩니다.

<BR><BR>



---
## Local Command

#### Run Application
````
docker build --build-arg SPRING_PROFILES_ACTIVE=local -t itwc-back . && 
docker run -v ./logs:/logs -p 8080:8080 itwc-back
````

#### Swagger
````
http://localhost:8080/swagger-ui/index.html
````

<br><br><br>

---