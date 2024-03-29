FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats-service.jar
#ENV TZ="Europa/Moscow"
ENTRYPOINT ["java","-jar","/stats-service.jar"]