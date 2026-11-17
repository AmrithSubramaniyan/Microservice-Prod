FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
# Copy customerservice and install it so orderservice can resolve the dependency
COPY customerservice ./customerservice
RUN cd customerservice && mvn clean install -DskipTests

# Now copy and build orderservice
COPY orderservice ./orderservice
RUN cd orderservice && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/orderservice/target/*.jar ./
RUN sh -c 'mv *exec.jar app.jar 2>/dev/null || mv *.jar app.jar'
ENTRYPOINT ["java", "-jar", "app.jar"]