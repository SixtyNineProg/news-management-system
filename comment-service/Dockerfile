FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
COPY build/libs/comment-service-1.0.0.jar .
ENTRYPOINT ["java","-jar","comment-service-1.0.0.jar"]