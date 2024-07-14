FROM openjdk:17
EXPOSE 8000
ADD target/springboot.jar springboot.jar
ENTRYPOINT ["java","-jar","springboot.jar"]