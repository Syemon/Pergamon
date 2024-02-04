FROM eclipse-temurin:21-jre-jammy

RUN addgroup usergroup; adduser  --ingroup usergroup --disabled-password appuser
RUN mkdir "uploads"
RUN chown appuser uploads
USER appuser

WORKDIR /opt

COPY target/Pergamnon.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]