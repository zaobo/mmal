FROM java:8
VOLUME /tmp
ADD protal-0.0.1-SNAPSHOT.jar protal-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=pro", "-Duser.timezone=GMT+08", "/protal-0.0.1-SNAPSHOT.jar"]