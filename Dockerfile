FROM azul/zulu-openjdk:22-jre

MAINTAINER github.com/jarvvski
COPY build/libs/*.jar service.jar
ENTRYPOINT ["java","-jar","/service.jar"]