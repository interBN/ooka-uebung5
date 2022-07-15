# Dockerize Liquid-Gas-System

```bash
mvn clean install
```

```bash
docker build -t ooka/liquid-gas-system .
```

```bash
docker run -p 8071:8071 ooka/liquid-gas-system
```