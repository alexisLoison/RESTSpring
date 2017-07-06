FROM davidcaste/alpine-java-unlimited-jce
EXPOSE 8888
VOLUME /tmp
ADD build/libs/Config-1.1.0.RELEASE.jar app.jar
RUN bash -c 'touch app.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]