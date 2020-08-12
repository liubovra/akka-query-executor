FROM openjdk:8-jdk-alpine

RUN apk add --update bash
RUN mkdir /etc/lib/
ADD lib/*.jar /etc/lib/
CMD java -cp /etc/lib/*.jar:/etc/lib/*:. com.example.StartupApp $DB_IP $DB_USER $DB_PASSWORD $DB_DRIVER $LIMIT_SIZE
