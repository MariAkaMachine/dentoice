FROM marschine/baseimage:latest
VOLUME /tmp
COPY build/libs/*.jar app.jar
EXPOSE 9876
ENTRYPOINT ["java","-jar","/app.jar"]