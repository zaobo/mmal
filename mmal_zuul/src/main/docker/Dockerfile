FROM java:8
VOLUME /tmp
ADD zuul-0.0.1-SNAPSHOT.jar zuul-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=pro", "-Duser.timezone=GMT+08","/zuul-0.0.1-SNAPSHOT.jar"]