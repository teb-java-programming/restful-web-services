## Useful Commands

### Maven

```
mvn wrapper:wrapper --> generates maven wrapper (mvnw)
mvn versions:display-dependency-updates --> generates a report on available pom dependency updates
mvn versions:use-latest-releases --> updates pom dependencies with latest versions
```

### Docker

```
docker compose up -d --> creates and starts services in background
docker compose down --> stop and remove containers
docker compose down -v --> deletes volumes
docker compose logs -f --> streams live logs from services
```

### Redis

```
docker exec -it cache-redis redis-cli --> opens Redis command line
docker exec -it cache-redis redis-cli FLUSHALL --> reset local Redis
```

## Useful Links

#### https://www.jwt.io
