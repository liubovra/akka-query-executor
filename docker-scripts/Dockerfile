FROM openjdk:8-jdk-alpine

RUN apk add --update bash
RUN mkdir /etc/lib/
ADD lib/*.jar /etc/lib/
COPY script.sh /tmp/script.sh
EXPOSE 8600
CMD /tmp/script.sh
