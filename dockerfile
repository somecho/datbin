FROM clojure:lein-2.10.0 as build

WORKDIR /server

COPY ./project.clj .

RUN lein deps

COPY ./src ./src
COPY ./config ./config

RUN lein uberjar

CMD ["java",\
    "--add-opens","java.base/java.nio=ALL-UNNAMED",\
    "--add-opens","java.base/sun.nio.ch=ALL-UNNAMED",\
    "-jar","target/server.jar"]