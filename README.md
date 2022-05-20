# ooka-uebung5

Based on: https://youtu.be/FdXDtLJgS9w

## Setup

- Install Docker on your system: https://www.docker.com/get-started/

- Pull Postgres: https://hub.docker.com/_/postgres

- Run Postgres using Docker:

```
docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
```

- Configure Database in Idea: https://www.youtube.com/watch?v=YRaHpCB8caw
