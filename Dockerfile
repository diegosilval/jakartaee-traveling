FROM payara/server-full:6.2024.9-jdk21
COPY target/traveling.war $DEPLOY_DIR
