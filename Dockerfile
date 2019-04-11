FROM openjdk:8
MAINTAINER sashapotapovich@gmail.com
COPY ./build/libs/ /tmp
WORKDIR /tmp
CMD java -jar testForDIFrmwrk-1.0-SNAPSHOT-all.jar