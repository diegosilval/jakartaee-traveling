FROM payara/server-full:6.2024.10-jdk21
ENV GOOGLE_API_KEY=YOUR_GOOGLE_API_KEY
COPY target/traveling.war $DEPLOY_DIR
