# Design

## Overview

La migracion se implementa conservando el contrato operativo de `ZcodeMain`, pero reemplazando las piezas internas que estaban atadas a Hibernate Tools 5.x y `javax.persistence`.

El nuevo flujo queda asi:

1. `ZcodeMain` carga configuracion y prepara el directorio temporal de entidades.
2. `ZathuraReverseEngineering` genera `hibernate.cfg.xml`, `build.xml` y `hibernate.reveng.xml` compatibles con Hibernate Tools 7.3.
3. Ant embebido ejecuta `HibernateToolTask` 7.3 y genera entidades temporales con `jakarta.persistence`.
4. `TemporaryJavaCompiler` compila esas entidades en proceso usando el classpath actual de Maven.
5. `JPAEntityLoaderEngine` y `MetaDataUtil` cargan metadata por reflexion desde anotaciones `jakarta.persistence`.
6. SkyJet genera el proyecto Spring Boot final.

## Key Decisions

- Se mantiene Ant embebido para minimizar ruptura en el reverse engineering.
- Se reemplaza solo la compilacion temporal legacy; no se reescribe la generacion de reveng con otra API.
- Se adopta `jakarta.persistence` de forma completa dentro del generador.
- Se eliminan dependencias locales versionadas a mano y se administran desde Maven.
- Los templates de reverse engineering se endurecen para ejecucion offline y para las expectativas estrictas de Hibernate Tools 7.3.

## Compatibility Adjustments

### Reverse engineering XML

- Se removieron `DOCTYPE` remotos para que el parser no dependa de `www.hibernate.org`.
- Los `table-filter` siempre emiten `match-catalog` y `match-schema` validos, usando `.*` cuando no aplica.
- MySQL usa `com.mysql.cj.jdbc.Driver`.

### Type mappings

- Los aliases simples como `String`, `Integer` o `Double` se reemplazan por clases Java completas donde Hibernate Tools 7.3 lo requiere.
- `NUMERIC` y `DECIMAL` se mapean a `java.math.BigDecimal` para evitar errores de resolucion de escala en tipos flotantes.

### Generated code templates

- Las plantillas de entidades y DTOs importan `java.math.*` para soportar `BigDecimal` en codigo generado.

## Validation

La migracion queda aceptada si pasa todo lo siguiente:

1. `mvn -DskipTests compile` en el generador.
2. `mvn -DskipTests package` en el generador.
3. Ejecucion directa de `org.zcode.generator.ZcodeMain` contra la base `bank` en PostgreSQL.
4. Generacion completa de entities, repositories, DTOs, mappers, services, controllers y archivos base del proyecto.
5. `mvn -DskipTests compile` en `/Users/dgomez/demo-bank`.

## Residual Risks

- Hibernate Tools 7.3 sigue reportando warnings de bindings duplicados en algunas relaciones reverse engineered.
- Los mappings numericos pueden requerir mas ajuste en otras bases con tipos no probados en esta validacion.
- El pipeline sigue dependiendo de Ant embebido para la fase de reverse engineering.
