FROM java:8
VOLUME /tmp
ADD protal-0.0.1-SNAPSHOT.jar protal-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=pro","/protal-0.0.1-SNAPSHOT.jar"]