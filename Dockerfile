FROM gradle:jdk10

RUN mkdir /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build

COPY /build/libs/Layer.jar /home/gradle/src

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Layer.jar"]


