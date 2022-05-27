# OOKA Übung 5

## Install

- Install Docker on your system: https://www.docker.com/get-started/

- Install Postman: https://www.postman.com/downloads/

- Install Multirun: https://plugins.jetbrains.com/plugin/7248-multirun/

![img.png](img/multirun.png)

## Setup DB

- Pull Postgres: https://hub.docker.com/_/postgres

- Create Postgres Container:

Algo-A:

```
docker run --name algoA -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres
```

Algo-B:

```
docker run --name algoB -e POSTGRES_PASSWORD=password -p 5433:5432 -d postgres
```

Analysis-Control:

```
docker run --name analysisControl -e POSTGRES_PASSWORD=password -p 5434:5432 -d postgres
```

CLI:

```
docker run --name cli -e POSTGRES_PASSWORD=password -p 5435:5432 -d postgres
```

- Stop Postgres container:

```
docker stop algoA
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