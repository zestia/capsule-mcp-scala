FROM eclipse-temurin:17
COPY capsule-mcp.jar /app/capsule-mcp.jar
ENTRYPOINT ["java", "-jar", "/app/capsule-mcp.jar"]