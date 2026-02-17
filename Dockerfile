FROM eclipse-temurin:17

RUN apt update && apt install -y curl

RUN curl https://raw.githubusercontent.com/VirtusLab/scala-cli/refs/heads/main/scala-cli.sh > /bin/scala && \
    chmod +x /bin/scala

WORKDIR /app

COPY project.scala /app/
COPY src/ /app/src/

RUN scala --power package . --assembly -o /app/capsule-mcp.jar

ENTRYPOINT ["java", "-jar", "/app/capsule-mcp.jar"]