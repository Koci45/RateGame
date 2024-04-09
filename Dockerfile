FROM openjdk:17-jdk

COPY target/RateGame-1.0.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "RateGame-1.0.jar"]