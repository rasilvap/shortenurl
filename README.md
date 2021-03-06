[![Actions Status](https://github.com/rasilvap/shortenurl/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/rasilvap/shortenurl/actions)

# shortenurl

## A Dockerized Spring Boot Service using Redis as Storage.

This app uses redis  due to the ability to be used as an in-memory data structure. Furthermore the Dictionary functionality we require can be obtained from Redis’ hset. This project is going to be moved to use a mongo Database, now is using redis but is too complex to use Object Oriented Design, besiedes that I have to design and build an app to monitorize the service. With the current design it is being guaranteed the scalability of the shorten App using a counter to define each new url and avoiding duplicates. Finally it must be handle by the Elastic Benstalk technology.
### Utils

#### IDUtils
This class has the next responsabilities:
1. Generating ID
2. Using ID to create an unique URL ID
3. Using unique URL ID to return original ID

#### URLValidatorUtils

This class is responsible for validating URL from the request.

### Repository

#### ShortURLRepository
Class responsible for interact with the redis database (read/write logic).

### Service

#### ShortURLService
Class used to abstract the URL Shortening and URL Retrieval logic.

### Controller

#### ShortenUrlController
The Controller responsible for interact with the users petitions with the next responsabilities:
1. Serving an endpoint to shorten URL
2. Redirect shortened URL to the original URL

## How to run.

This app is builded using maven and docker, make sure that you have installed this tools. The process to run the application is pretty simple, please run the next commands:

1. Clone the repo in your local machine with git: git clone https://github.com/rasilvap/shortenurl.git
2. Move to the root of the proyect /shortenurl.
3. mvn clean install
4. docker build -f Dockerfile -t shortenurl .
5. docker-compose up

By default the Server will run on localhost:8080/shorten

To test, send POST Request to localhost:8080/shorten with a body of type application/json with body. You can use postman to make this petition, for example, you can use the next BodyRequest:

```
{
  "url" : "https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/"
}
```

This request is going to give you, the next response:

```
http:localhost:8080/v
```

If you use the get request with this shortenurl, you are going to have the original url.


#### ShortenUrlController


## Tech Design ShortenUrl.

### shortenURL
<img src = "src/main/java/com/neueda/shorturl/web/images/shortenURL.png" />


### retrieveOriginalUrl

<img src = "src/main/java/com/neueda/shorturl/web/images/retrieveOriginalURL.png" />

