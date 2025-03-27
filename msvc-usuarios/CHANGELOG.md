
# 0.0.3 

- Se guardan los logs en archivo

# 0.0.2

- Para poder enviar comandos desde fuera del contenedor cambiamos ENTRYPOINT por CMD en Dockerfile
```
CMD ["java","-jar","msvc-usuarios-0.0.2.jar"]
```

# 0.0.1-SNAPSHOT

### Para ejecutar el microservicio usuarios, con la bd afuera del contenedor,

```
docker run --add-host=host.docker.internal:host-gateway -p 8001:8001 usuarios
```
