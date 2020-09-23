FROM openjdk:14-alpine
COPY build/libs/calorieapp-*-all.jar calorieapp.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "calorieapp.jar"]