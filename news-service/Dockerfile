FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
COPY build/libs/news-service-1.0.0.jar .
ENTRYPOINT ["java","-jar","news-service-1.0.0.jar"]