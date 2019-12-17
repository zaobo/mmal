FROM java:8
VOLUME /tmp
ADD manager-0.0.1-SNAPSHOT.jar manager-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=pro", "-Duser.timezone=GMT+08","/manager-0.0.1-SNAPSHOT.jar"]