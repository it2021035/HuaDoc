FROM openjdk:21-rc-oracle
MAINTAINER it2021089
WORKDIR /app
COPY target/localDocWebApp-0.0.1-SNAPSHOT.jar ./application.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","application.jar"]