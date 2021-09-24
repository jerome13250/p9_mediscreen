## This uses multi-stage build :
## https://docs.docker.com/develop/develop-images/multistage-build/

## Need to add curl to the alpine for healthcheck in docker-compose.yml file

###########################
#microservice mpatient
###########################
# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine AS mpatient
# Install curl for healthcheck
RUN apk --no-cache add curl
# Add folder :
RUN mkdir /tmp/app
# copy JAR into image
COPY ./mpatient/target/mpatient-0.0.1-SNAPSHOT.jar /tmp/app
#api port
EXPOSE 8081
# run java app:
CMD java -jar /tmp/app/mpatient-0.0.1-SNAPSHOT.jar

###########################
#Client UI
###########################
# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine AS clientui
# Install curl for healthcheck
RUN apk --no-cache add curl
# Add folder :
RUN mkdir /tmp/app
# copy JAR into image
COPY ./clientui/target/clientui-0.0.1-SNAPSHOT.jar /tmp/app
#api port
EXPOSE 8081
# run java app:
CMD java -jar /tmp/app/clientui-0.0.1-SNAPSHOT.jar