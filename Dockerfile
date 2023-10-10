FROM openjdk:17

EXPOSE 8080

COPY target/bankAPI-0.0.1-SNAPSHOT.jar /bank-api.jar

CMD ["java", "-jar", "bank-api.jar"]