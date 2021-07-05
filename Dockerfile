FROM amazoncorretto:11.0.9-alpine

EXPOSE 8081

RUN echo "networkaddress.cache.ttl=5" >> /${JAVA_HOME}/conf/security/java.security

ENTRYPOINT java --show-version -server -noverify -XX:MaxRAMPercentage=80.0 -XX:+UseSerialGC -XX:+HeapDumpOnOutOfMemoryError -jar application.jar

COPY build/libs/application.jar application.jar
