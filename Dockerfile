FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar mobileshop.jar
ENTRYPOINT ["java","-jar","/mobileshop.jar"]
EXPOSE 8080