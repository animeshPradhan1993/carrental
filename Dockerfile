FROM eclipse-temurin:21-jre-alpine AS runtime
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -XX:+HeapDumpOnOutOfMemoryError"
WORKDIR /app
RUN addgroup -S app && adduser -S app -G app
COPY target/app-*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
