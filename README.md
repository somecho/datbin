> 📚 Disclaimer: This is a weekend project with the purpose of learning [Pedestal](http://pedestal.io/pedestal/0.7-pre/index.html) and, associated with it, interceptors.

# Datbin
### Datbin is a simple self-hosted pastebin for all data formats. Here's how it works.
Assuming Datbin is running somewhere (for our purposes, let's say it is `localhost`), the home page serves an upload form, from which you can upload your file. If the upload is successful, you'll be redirected to a page confirming your success and be given a link, with which you can share your file. 

<p align="center"><img width="480px" src="https://github.com/somecho/datbin/assets/26333602/092f06f0-cd6b-43b2-8393-188d70f18c7b"/></p>
<p align="center"><em>upload form</em></p>
<p align="center"><img width="480px" src="https://github.com/somecho/datbin/assets/26333602/5986b2dc-78e2-4b96-828a-2fd1970c999c"/></p>
<p align="center"><em>success page</em></p>

#### Usecases
Since Datbin is, in our scenario, running in a local network, you can only really share it if you know the private IP address of the machine Datbin is running on and if the device you are trying to access the shared file with is also in the same local network. One way of accessing the data from external networks is to use a VPN like [Tailscale](https://tailscale.com) and have both machines be connected to it. 

## Technical Details
Datbin is written in Clojure with the web library Pedestal. The pages, for which little effort was spent to make stylish, are rendered server-side with [Hiccup](https://github.com/weavejester/hiccup) and [Garden](https://github.com/noprompt/garden). [Datalevin](https://github.com/juji-io/datalevin) is used as the records-storing database, keeping track of the uploads.



### Usage
Start the server and go to `localhost:3888`.

#### Leiningen
```sh
lein run
```
#### Jar
```sh
lein uberjar
java \
  --add-opens java.base/java.nio=ALL-UNNAMED \
  --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
  -jar target/server.jar
```
### Docker
#### Building with Docker
```
docker build -t datbin .
docker run -p 3888:3888 -v $PWD/data:/server/data:rw -v $PWD/logs:/server/logs:rw datbin
```
#### Docker Compose
```
docker-compose up
```

---

© 2023 Somē Cho

