# Description
Pergamon is a simple API used for storing, downloading and querying resources from URL addresses.

## Features
There is OpenAPI documentation available at `http://localhost:48080/swagger-ui/index.html` after running the application.
### Resource create
Metadata are stored in PostgresSQL database. Files are stored in file system with UUID name to prevent file conflicts. Resource is being created asynchronously.
### Resource list
Resource listing uses HATEOS architecture. Each object contains information about resource and generated link to download endpoint
### Resource query
Resource querying uses resource list endpoint with query string parameter. Query searches resources by url fragments
### Resource download
Resources are downloaded by given URL address. If the resource does not exist then application returns error

# Instalation

Install [docker-compose](https://docs.docker.com/compose/install/).
* Download project from repository: `git clone https://github.com/Syemon/Pergamon.git`
* Go inside directory: `cd Pergamon`
* Build application: `mvn install`
* Run application: `docker-compose -f docker-local/docker-compose.yml up --build`










