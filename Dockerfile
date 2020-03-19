FROM openjdk:11-jdk AS BUILD
WORKDIR /mopsBuild
COPY . .
RUN rm /mopsBuild/src/main/resources/application.properties
RUN mv /mopsBuild/src/main/resources/application-production.properties /mopsBuild/src/main/resources/application.properties
RUN ./gradlew bootJar

FROM openjdk:11-jre
WORKDIR /mops
COPY --from=BUILD /mopsBuild/build/libs/*.jar .
COPY ./wait-for-it.sh .
RUN chmod +x wait-for-it.sh
ENTRYPOINT ./wait-for-it.sh -t 10 db:3306 -- java -jar mops.jar
