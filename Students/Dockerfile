FROM java:8
EXPOSE 1111
VOLUME /tmp
ADD build/libs/Students-1.1.0.RELEASE.jar app.jar
RUN bash -c 'touch app.jar'
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongodb/microservices", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]