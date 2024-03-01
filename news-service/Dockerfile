FROM openjdk:17
VOLUME /tmp
EXPOSE 8082
COPY build/libs/news-service-1.0.0.jar .
ENTRYPOINT ["java","-jar","news-service-1.0.0.jar"]