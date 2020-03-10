FROM openjdk:11-jdk AS BUILD
WORKDIR /mopsBuild
COPY . .
RUN ./gradlew bootJar

FROM openjdk:11-jre
WORKDIR /mops
COPY --from=BUILD /mopsBuild/build/libs/*.jar .
COPY ./wait-for-it.sh .
RUN chmod +x wait-for-it.sh
ENTRYPOINT ./wait-for-it.sh -t 10 db:5432 -- java -jar mops.jar --spring.config.location=file:/src/main/resources/application-production.properties
