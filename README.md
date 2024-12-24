# REST API with Spring Boot 3 and PostgreSQL secured with Spring Security (JWT + RBAC)

This application is written with **Spring Boot 3** and uses **PostgreSQL** as the database.
It uses Spring Security with JWT for authentication and authorization (RBAC).

Includes tests written with Mockito and MockMvc.

## API Documentation
The API documentation can be viewed at the following link: [API Documentation](https://documenter.getpostman.com/view/32162797/2sAYJ4hfei)

### Live Deployment

[Deployment Link](https://stay-ease-production.up.railway.app/)

## Setup
1. Clone the repository.
2. Navigate to the `docker` folder.
3. Run `docker-compose up`.

```sh
cd docker
docker-compose up
```

## Setup (Build and Run)

```sh
.\mvnw spring-boot:run
```