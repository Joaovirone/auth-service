FROM eclipse-temurin

WORKDIR /app

COPY build.gradle.kts .

RUN 

COPY src ./src

RUN 


