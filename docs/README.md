# Helpful Information

## Remove dangling images

```bash
docker rmi $(docker images --filter "dangling=true" -q --no-trunc) -f
```