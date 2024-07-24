FROM openjdk:17
ADD build/libs/HealthCare-POC-0.0.1-SNAPSHOT.jar HealthCare-POC-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/HealthCare-POC-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
