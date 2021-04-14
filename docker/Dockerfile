FROM 32918698.dkr.ecr.us-east-1.amazonaws.com/baseorajava:79ui8-appdyn-98214
VOLUME /tmp
ADD *.jar app.jar
RUN sh -c 'touch /app.jar'
COPY /docker-entrypoint.sh /
RUN ["chmod", "+x", "docker-entrypoint.sh"]
ENV JAVA_OPTS=""
ENTRYPOINT ["/docker-entrypoint.sh"]