FROM java:8
VOLUME /tmp
ADD build/libs/Zuul-1.1.0.RELEASE.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8765
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]