FROM openjdk:17

COPY target/*.jar /app/

WORKDIR /app

EXPOSE 6050

ENTRYPOINT ["java", "-jar", "SpringbootMysqlproject-0.0.1-SNAPSHOT.jar"]





#FROM openjdk:8u151-jdk-alpine3.7

#EXPOSE 8070 

#ENV APP_HOME /app

#COPY target/SpringbootMysqlproject-0.0.1-SNAPSHOT.jar $APP_HOME/app.jar

#WORKDIR $APP_HOME

#ENTRYPOINT exec java -jar app.jar


# FROM openjdk

# COPY target/*.jar /

# EXPOSE 8080 

# ENTRYPOINT ["java","-jar","/SpringbootMysqlproject-0.0.1-SNAPSHOT.jar"]

