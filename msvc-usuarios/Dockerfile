FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/msvc-usuarios

COPY ./pom.xml /app
COPY ./msvc-usuarios/.mvn ./.mvn
COPY ./msvc-usuarios/mvnw .
COPY ./msvc-usuarios/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target

COPY ./msvc-usuarios/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.3.jar .

EXPOSE 8001

CMD ["java","-jar","msvc-usuarios-0.0.3.jar"]


#ARG JAR_FILE=target/*.jar
#
#COPY ${JAR_FILE} msvc-usuarios.jar
#
#ENTRYPOINT ["java","-jar","/msvc-usuarios.jar"]