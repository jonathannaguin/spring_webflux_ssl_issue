# Description

This project shows a performance issue when using SSL with a Spring Webflux service with Reactor Netty.

## Running the service

Create a Boot JAR first.
```
$ ./gradlew bootJar  
```

Build the Docker image.

```
$ docker build -t demo . 
```

And run it.
```
$ docker run -p 8081:8081 --cpus=1 --memory=2g -e SPRING_PROFILES_ACTIVE=dev demo
```

## Performance test

On another shell, run Gatling, you can change the URL and the number of requests per seconds you want to achieve with Gatling. 
```
$ URL=https://localhost:8081 REQUEST_PER_SECOND=100 ./gradlew gatlingRun 
```

## The issue

- The `application.yml` contains configuration for SSL with a self-signed certificate.
- If SSL is disabled, the service is able to handle 150 requests per seconds without any issues.
- When SSL is enabled, the Gatling test will fail after a few seconds with plenty of SSL handshake errors.
- In order to get a clean run with SSL, we would need to lower the number of requests per second to 50.

NOTE: Numbers can vary slightly depending on your machine.
