FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar server.jar
#ENV TZ="Europa/Moscow"
ENTRYPOINT ["java","-jar","/server.jar"]