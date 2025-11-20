# `README.md`

## Overview
Car Rental multi\-module project (parent `carrental`) using Spring Boot 3\.5\.7, Java 21, modules: `model` (API generated / shared models) and `app` (service, controllers, persistence).

## Modules
\- `model`: Holds OpenAPI generated classes (`CarRentalBooking` etc.).
\- `app`: Spring Boot application (controllers, entities, services, mappers, security).

## Tech Stack
\- Java 21
\- Spring Boot (Web, Security, Data JPA)
\- MapStruct for DTO/entity mapping
\- springdoc\-openapi for OpenAPI spec
\- Maven (parent POM manages versions)

## Build
```bash
mvn clean install 
or
./mvnw clean install



## Run App
cd app
mvn spring-boot:run
or
../mvnw spring-boot:run

## Application uses basic auth the credentials are configured in application.yml
