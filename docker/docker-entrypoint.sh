#!/bin/bash
export ECS_INSTANCE_ID=$(curl -s url)
export APPDYNAMICS_NODE_NAME=${APPDYNAMICS_NODE_NAME}_${ECS_INSTANCE_ID}
export APPDYNAMICS_AGENT_UNIQUE_HOST_ID=${APPDYNAMICS_AGENT_APPLICATION_NAME}_${hostname}_${ECS_INSTANCE_ID}
export APPD_OPTS="java agent appdynamics agent"
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom $APPD_OPTS -jar /app.jar