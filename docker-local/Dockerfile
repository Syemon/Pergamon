FROM eclipse-temurin:17-jre-jammy

RUN addgroup usergroup; adduser  --ingroup usergroup --disabled-password appuser

WORKDIR /opt
RUN mkdir "uploads"
RUN chown appuser uploads
USER appuser

COPY Pergamnon.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]