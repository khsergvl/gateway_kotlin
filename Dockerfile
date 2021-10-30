FROM openjdk:11
COPY ./target/gateway-0.0.1-SNAPSHOT.jar /usr/src/gateway/
WORKDIR /usr/src/gateway
CMD ["java", "-jar", "gateway-0.0.1-SNAPSHOT.jar"]