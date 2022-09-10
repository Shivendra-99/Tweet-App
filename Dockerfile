FROM openjdk:11
EXPOSE 5000
ADD target/Tweet-App.jar Tweet-App.jar 
ENTRYPOINT ["java","-jar","/Tweet-App.jar"]
