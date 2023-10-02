FROM openjdk:8
LABEL maintainer="kapilbodhare"
ADD target/processPension-0.0.1-SNAPSHOT.jar process-pension-service.jar
ENTRYPOINT ["java", "-jar", "process-pension-service.jar"]