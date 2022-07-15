# Dockerize Power-Systems

```bash
mvn clean install
```

```bash
docker build -t ooka/power-systems .
```

```bash
docker run -p 8070:8070 ooka/power-systems
```