FROM openjdk:21-rc-oracle as builder
WORKDIR /app

# Copying Maven wrapper and setting executable permissions
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw

# Copy the source code and package the application
COPY ./src ./src
RUN ./mvnw package -Dmaven.test.skip

FROM openjdk:19-jdk-alpine3.16
RUN apk update && apk add curl

WORKDIR /app

# Setting up non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copying the built jar from the builder stage
COPY --from=builder /app/target/*.jar /app/*.jar

# Expose port and set entrypoint
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/*.jar"]
