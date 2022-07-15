# Dockerize Analysis-Control

```bash
mvn clean install
```

```bash
docker build -t ooka/analysis-control .
```

```bash
docker run -p 8072:8072 ooka/analysis-control
```