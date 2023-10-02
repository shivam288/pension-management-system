FROM openjdk:8
LABEL maintainer="kapilbodhare"
ADD target/authorization-0.0.1-SNAPSHOT.jar authorization-service.jar
ENTRYPOINT ["java", "-jar", "authorization-service.jar"]