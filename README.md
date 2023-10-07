## "HELLO, ITWC Backend" 

<BR><BR>



#### Run Application Command (LOCAL)
````
docker build --build-arg SPRING_PROFILES_ACTIVE=local -t itwc-back . && docker run -p 8080:8080 itwc-back
````