# shortenurl

## A Dockerized Spring Boot Service with usinf Redis as Storage.

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



