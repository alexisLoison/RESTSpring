FROM java:8
EXPOSE 8761
VOLUME /tmp
ADD build/libs/Eureka-1.1.0.RELEASE.jar app.jar
RUN bash -c 'touch app.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]