FROM openjdk:11-jdk AS BUILD
WORKDIR /mopsBuild
COPY . .
RUN ./gradlew bootJar

FROM openjdk:11-jre
WORKDIR /mops
COPY --from=BUILD /mopsBuild/build/libs/*.jar .
CMD java -jar mops.jar --spring.config.location=file:/src/main/resources/application-production.properties