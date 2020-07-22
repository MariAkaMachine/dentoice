# Dentoice Backend Service

## dev new version

### change application properties
- add default password to datasource & mail
- enable liquibase and set drop-first to _true_
- change hibernate ddl-auto to _create_

### committing changes
- never commit the adapted application properties

### test service
- start postgres container
- keep application properties
- run service ide
- run `./gradlew clean build`

### prepare upgrade
- __disable__ liquibase and set drop-first to _false_
- change hibernate auto-ddl to _validate_
- run `./gradlew clean build -xtest` to build drop and skip failing tests
- build docker image `docker build -t marschine/dentoice-service .`
- push docker image `docker push marschine/dentoice-service`

## upgrade old setup

### initial database setup
- `docker run --name dentoice-postgres -d -v /home/edith/Schreibtisch/dentoice:/postgresql/data -p 5432:5432 -e POSTGRES_PASSWORD=<pw> -e POSTGRES_USER=dentoice postgres:10.6-alpine`
- `docker run --name postgres-smoke -d -v /home/edith/Schreibtisch/smoke:/postgresql/data -p 5432:5432 -e POSTGRES_PASSWORD=<pw> -e POSTGRES_USER=dentoice postgres:10.6-alpine`

### pull image 
- `docker tag marschine/dentoice-service:latest marschine/dentoice-service:old`
- `docker pull marschine/dentoice-service`

### run new image
- `docker rename dentoice-service dentoice-service-old`
- `docker start postgres-smoke`
- `docker run -i -p 9876:9876 --name dentoice-service marschine/dentoice-service`
