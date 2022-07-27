FROM openjdk:11
MAINTAINER eden
ARG JAR_FILE
COPY ${JAR_FILE}} /root/${JAR_FILE}
EXPOSE 8080 8080
ENTRYPOINT ["java","-server -Xms1G -Xmx1G -Xmn512M -Xss1M -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=128M","-jar", "--spring.config.activate.on-profile=prod","/roo/${JAR_FILE}"]
