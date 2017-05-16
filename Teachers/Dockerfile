FROM java:8
VOLUME /tmp
ADD build/libs/Teachers-1.1.0.RELEASE.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 2222
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongodb/microservices", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]