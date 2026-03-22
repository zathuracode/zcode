## Why

El pipeline de reverse engineering de Zathuracode dependia de `org.hibernate:hibernate-tools` 5.x, `javax.persistence` y una compilacion temporal legacy apoyada en Ant y un jar local de JPA. Esa base ya no era coherente con el estado actual del generador, que produce codigo Spring Boot moderno, usa Java 25 y necesitaba seguir modernizandose sin perder el flujo end to end.

La migracion a Hibernate Tools 7.3 era necesaria para:

- alinear el reverse engineering con una linea vigente de Hibernate Tools;
- mover el generador por completo a `jakarta.persistence`;
- eliminar dependencias locales versionadas a mano;
- mantener generacion reproducible y validada contra una base real.

## What Changes

Este cambio migra el pipeline de reverse engineering a Hibernate Tools 7.3.0.Final y actualiza el generador para operar completamente con `jakarta.persistence`.

La implementacion incluye:

- reemplazo de dependencias legacy por artifacts `org.hibernate.tool:*` 7.3.0.Final;
- migracion de la lectura de metadata desde `javax.persistence` a `jakarta.persistence`;
- reemplazo del flujo de compilacion temporal basado en `buildCompile.xml` por compilacion en proceso con `JavaCompiler`;
- eliminacion del jar local `hibernate-jpa-2.1-api-1.0.2.Final.jar`;
- adaptacion de templates `hibernate.cfg.xml` y `hibernate.reveng.xml` para compatibilidad con Hibernate Tools 7.3 y ejecucion offline;
- ajuste de mappings numericos e imports en templates generados para que el proyecto resultante compile.

## Capabilities

### Added

- Capacidad para ejecutar reverse engineering con Hibernate Tools 7.3.0.Final.
- Capacidad para procesar entidades temporales anotadas con `jakarta.persistence`.
- Capacidad para compilar entidades temporales en proceso sin depender de jars locales o un build Ant adicional.

### Modified

- La generacion temporal de entidades ahora usa `jakarta.persistence` en vez de `javax.persistence`.
- El flujo de reverse engineering ya no depende de DTDs remotos ni de `buildCompile.xml`.

## Impact

El generador mantiene el mismo entrypoint y la misma configuracion externa, pero cambia internamente de stack JPA y reverse engineering. El resultado esperado es que el equipo siga ejecutando `ZcodeMain` igual que antes, con una base mas moderna y menos fragil.
