FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stat-server.jar
#ENV TZ="Europa/Moscow"
ENTRYPOINT ["java","-jar","/stat-server.jar"]