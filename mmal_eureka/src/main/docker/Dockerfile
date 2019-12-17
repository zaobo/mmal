FROM java:8
VOLUME /tmp
ADD eureka-0.0.1-SNAPSHOT.jar eureka-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=pro", "-Duser.timezone=GMT+08", "/eureka-0.0.1-SNAPSHOT.jar"]