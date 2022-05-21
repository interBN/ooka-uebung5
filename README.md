# ooka-uebung5

Based on: https://youtu.be/FdXDtLJgS9w

## Setup

- Install Docker on your system: https://www.docker.com/get-started/

- Install Postman: https://www.postman.com/downloads/

- Configure Database in Idea Ultimate: https://www.youtube.com/watch?v=YRaHpCB8caw

## DB

- Pull Postgres: https://hub.docker.com/_/postgres

- Run Postgres using Docker:

```
docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
```

Stop Postgres:

```
docker stop some-postgres
```

microservices: https://sofienebk.medium.com/building-microservices-with-spring-boot-753be739ca7d
