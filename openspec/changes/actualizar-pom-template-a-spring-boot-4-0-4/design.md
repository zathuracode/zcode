# Design

## Overview

La estrategia para este cambio es "usar el BOM de Spring Boot primero, fijar manualmente solo lo externo". El template generado debe quedar mas simple y depender menos de versiones escritas a mano.

## Dependency Strategy

### Managed by Spring Boot

Las dependencias y plugins que ya administra Spring Boot 4.0.4 deben evitar version explicita en el template, salvo que una parte del build lo requiera fuera del alcance del BOM.

### Managed manually

Las versiones que se mantienen como propiedades en el template son:

- `org.mapstruct.version`
- `org.projectlombok.version`
- `lombok.mapstruct.binding.version`
- `springdoc.openapi.starter.webmvc.ui.version`
- `org.apache.commons.collections4.version`
- `io.jsonwebtoken.version`
- `org.jacoco.maven.plugin.version`
- `org.openclover.clover.maven.plugin.version`
- `org.pitest.maven.version`
- `org.pitest.junit5.plugin.version`

Se mantiene `lombok` con version explicita en `annotationProcessorPaths` porque el plugin del compilador necesita una coordenada concreta ahi, aunque la dependencia principal siga sin version.

## Template Changes

- Actualizar el parent a Spring Boot `4.0.4`.
- Cambiar `springdoc` a `3.0.2`.
- Actualizar `org.projectlombok.version` a `1.18.44`.
- Conservar `mapstruct` `1.6.3`, `lombok-mapstruct-binding` `0.2.0`, `jjwt` `0.13.0`, `pitest` y `clover` mientras sigan validando con el proyecto generado.
- Mantener `commons-collections4` con version explicita porque el proyecto generado no recibe esa dependencia desde el BOM de Spring Boot 4.0.4 en este template.
- Simplificar `maven-compiler-plugin` a `release=${java.version}` manteniendo `annotationProcessorPaths` y `compilerArgs`.

## Validation

Este cambio queda aceptado si:

1. `ZcodeMain` genera un proyecto real con la base `bank`.
2. El proyecto generado usa `spring-boot-starter-parent` `4.0.4`.
3. `mvn -DskipTests compile` pasa en el proyecto generado.
4. `mvn -DskipTests test-compile` pasa en el proyecto generado.

## Residual Risks

- `springdoc` 3.0.x puede introducir cambios de compatibilidad menores en runtime aunque el build compile.
- Plugins de calidad como Clover y PIT pueden requerir una modernizacion posterior si aparecen incompatibilidades fuera del flujo de compile/test-compile.
