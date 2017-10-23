FROM java:8
ENV APP_HOME /usr/local/JTServer
ENV PATH $APP_HOME:$PATH
RUN mkdir -p "$APP_HOME"
WORKDIR $APP_HOME
COPY JTServer/ $APP_HOME
EXPOSE 9835
CMD ["server","start","--no-daemon"]
