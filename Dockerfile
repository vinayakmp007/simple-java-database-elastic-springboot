FROM java:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY wrapper.sh wrapper.sh
RUN bash -c 'chmod +x /wrapper.sh'
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["/bin/bash", "/wrapper.sh"]
