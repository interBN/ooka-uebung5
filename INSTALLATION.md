## Install

- Docker (Windows): https://www.docker.com/get-started/

- Docker (Mac): https://formulae.brew.sh/formula/colima

- Pull Postgres: https://hub.docker.com/_/postgres

- Optional: Postman: https://www.postman.com/downloads/

- Optional: Multirun: https://plugins.jetbrains.com/plugin/7248-multirun/

![](img/multirun.png)

## Create all Postgres Container

```
docker run --name powerSystems -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres;docker run --name liquidGasSystems -e POSTGRES_PASSWORD=password -p 5433:5432 -d postgres;docker run --name analysisControl -e POSTGRES_PASSWORD=password -p 5434:5432 -d postgres;docker run --name cli -e POSTGRES_PASSWORD=password -p 5435:5432 -d postgres;docker run --name managementSystems -e POSTGRES_PASSWORD=password -p 5436:5432 -d postgres
```

Windows:

```
docker run --name powerSystems -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres&docker run --name liquidGasSystems -e POSTGRES_PASSWORD=password -p 5433:5432 -d postgres&docker run --name analysisControl -e POSTGRES_PASSWORD=password -p 5434:5432 -d postgres&docker run --name cli -e POSTGRES_PASSWORD=password -p 5435:5432 -d postgres&docker run --name managementSystems -e POSTGRES_PASSWORD=password -p 5436:5432 -d postgres
```

## Start all Postgres Container (Mac):

```
colima start &&
docker start powerSystems && 
docker start liquidGasSystems && 
docker start analysisControl && 
docker start cli &&
docker start managementSystems
```

## Start all Postgres Container (Windows):

```
docker start powerSystems && 
docker start liquidGasSystems && 
docker start analysisControl && 
docker start cli &&
docker start managementSystems
```

## Stop all Postgres Container:

```
docker stop $(docker ps -a -q --filter ancestor=postgres)
```

## Restart all Postgres Container:

```
docker restart $(docker ps -a -q --filter ancestor=postgres)
```

## Configure Database in Idea Ultimate

Go here:

![](img/db_0.png)

Add the password ("password") in each data source and test the connection:

![](img/db_1.png)

Result:

![](img/db_2.png)

## More Links

- Microservices: https://sofienebk.medium.com/building-microservices-with-spring-boot-753be739ca7d

- 3.3. Controller Layer: https://www.javadevjournal.com/spring-boot/spring-boot-resttemplate/

- Banner: https://springhow.com/spring-boot-banner-generator/
