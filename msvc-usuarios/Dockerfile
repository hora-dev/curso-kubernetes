ARG MSVC_NAME=msvc-usuarios

FROM openjdk:17-jdk-alpine as builder

ARG MSVC_NAME

WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target

COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.3.jar .
ENV PORT 8001
EXPOSE $PORT

CMD ["java","-jar","msvc-usuarios-0.0.3.jar"]


#ARG JAR_FILE=target/*.jar
#
#COPY ${JAR_FILE} msvc-usuarios.jar
#
#ENTRYPOINT ["java","-jar","/msvc-usuarios.jar"]