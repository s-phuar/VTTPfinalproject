# Build angular
FROM node:23 AS ng-build

#ghost directory in docker container
WORKDIR /src

RUN npm i -g @angular/cli

COPY client/public public
COPY client/src src
COPY client/*.json .
# COPY ALL json files

RUN npm ci
RUN ng build


#Build spring boot
FROM openjdk:23-jdk AS j-build

WORKDIR /src
COPY server/.mvn .mvn
COPY server/src src
COPY server/mvnw .
COPY server/pom.xml .

# copy from angular 
COPY --from=ng-build /src/dist/client/browser/ src/main/resources/static
# COPY --from=ng-build /src/dist/client-side/browser/* src/main/resources/static

#compile mvn project and run as executable
RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

#copy the JAR file over to the final container
FROM openjdk:23-jdk

WORKDIR /app
COPY --from=j-build /src/target/server-0.0.1-SNAPSHOT.jar app.jar
COPY server/couchbase-cert.pem.txt /app/couchbase-cert.pem

ENV PORT=8080

ENV SPRING_DATASOURCE_URL=
ENV SPRING_DATASOURCE_USERNAME=
ENV SPRING_DATASOURCE_PASSWORD=

ENV SPRING_COUCHBASE_CONNECTION_STRING=
ENV SPRING_COUCHBASE_USERNAME=
ENV SPRING_COUCHBASE_PASSWORD=
ENV SPRING_DATA_COUCHBASE_BUCKET_NAME=

ENV SPRING_DATA_REDIS_HOST=
ENV SPRING_DATA_REDIS_PORT=
ENV SPRING_DATA_REDIS_USERNAME=
ENV SPRING_DATA_REDIS_PASSWORD=

ENV JWT_SECRET_KEY=

ENV SPRING_MAIL_HOST=
ENV SPRING_MAIL_PORT=
ENV SPRING_MAIL_USERNAME=
ENV SPRING_MAIL_PASSWORD=
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=

ENV STOCK_API_KEY=

ENV STRIPE_SECRET_KEY=

ENV SPRING_SECURITY_USER_NAME=
ENV SPRING_SECURITY_USER_PASSWORD=
ENV MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=

ENV TELEGRAM_BOT_TOKEN=
ENV TELEGRAM_CHAT_ID=


EXPOSE ${PORT}

SHELL ["/bin/sh", "-c"]

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar


