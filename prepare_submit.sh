#!/bin/bash
mvn clean install

docker build -t acp_cw1 .

docker image save acp_cw1 -o acp_submission_image.tar

rm -rf submit/*
rm /home/chenyang/Desktop/s2693586.zip

cp -r src/ acp_submission_image.tar Dockerfile mvnw mvnw.cmd pom.xml submit/

zip -r /home/chenyang/Desktop/s2693586.zip submit/*