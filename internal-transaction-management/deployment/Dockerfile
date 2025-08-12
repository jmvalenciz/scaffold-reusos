# Use debian based image for building
FROM eclipse-temurin:17-jdk-noble AS builder
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

WORKDIR /opt/app

COPY . .

# Make wrapper executable
RUN chmod +x ./gradlew

RUN ./gradlew --no-daemon --no-parallel --stacktrace assemble

# Build jar file
RUN ./gradlew --no-daemon --stacktrace bootJar

FROM eclipse-temurin:17-jre-noble AS runtime
VOLUME /tmp

# Create a non-root user for running the app
ARG APP_USER=appuser
ARG APP_HOME=/opt/app
# Replace with a non-root user to avoid running the container with excessive privileges
RUN useradd -ms /bin/bash ${APP_USER}
USER ${APP_USER}
WORKDIR ${APP_HOME}

# Copy artifact from build stage
COPY --from=builder ${APP_HOME}/applications/app-service/build/libs/*.jar app.jar

# Update artifact's ownership
# RUN chown ${APP_USER}:${APP_USER} ${APP_HOME}/app.jar

EXPOSE 8080

ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar app.jar" ]