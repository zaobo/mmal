FROM java:8
VOLUME /tmp
ADD provider_db-0.0.1-SNAPSHOT.jar provider_db-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=pro", "-Duser.timezone=GMT+08","/provider_db-0.0.1-SNAPSHOT.jar"]