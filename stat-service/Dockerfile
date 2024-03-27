FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar app.jar
#ENV TZ="Europa/Moscow"
ENTRYPOINT ["java","-jar","/app.jar"]