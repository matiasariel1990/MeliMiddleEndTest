FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/Middleend_Matias_Figueroa-1.0.0.jar /app/mfigueroa_meli_test.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/mfigueroa_meli_test.jar"]