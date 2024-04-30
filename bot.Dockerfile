FROM openjdk:21
WORKDIR /bot
COPY /bot/target/bot.jar bot.jar
EXPOSE 8090
ENTRYPOINT java -jar /bot/bot.jar
