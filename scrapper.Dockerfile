FROM openjdk:21
WORKDIR /scrapper
COPY /scrapper/target/scrapper.jar scrapper.jar
EXPOSE 8080
ENTRYPOINT java -jar /scrapper/scrapper.jar
