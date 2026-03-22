## Why

El template `pom.xml.vm` todavia genera proyectos con Spring Boot `4.0.0` y conserva varias versiones manuales que hoy pueden simplificarse o actualizarse. Eso hace que el proyecto generado envejezca mas rapido, arrastre overrides innecesarios y requiera mas mantenimiento manual del que deberia.

Spring Boot `4.0.4` ya esta publicado como version estable y ofrece una base mas reciente para los proyectos generados. El template debe alinearse con esa version y usar el BOM de Spring Boot siempre que sea posible, manteniendo solo las versiones externas que realmente no cubre Boot.

## What Changes

Este cambio actualiza `templates/skyJet/pom.xml.vm` para generar proyectos basados en Spring Boot `4.0.4` y limpia el manejo de versiones del template.

La actualizacion incluye:

- subir el parent a `spring-boot-starter-parent` `4.0.4`;
- reducir propiedades de version redundantes cuando el BOM de Spring Boot ya administra la dependencia;
- actualizar librerias externas compatibles necesarias para el template generado;
- documentar en OpenSpec que dependencias quedan bajo el BOM de Boot y cuales siguen fijadas manualmente.

## Capabilities

### Added

- Capacidad para generar proyectos alineados con Spring Boot `4.0.4`.
- Capacidad para usar el BOM de Spring Boot como fuente principal de versionado en el template.

### Modified

- El template del `pom.xml` reduce overrides de version.
- `springdoc` se mueve a una linea 3.0.x compatible con Spring Boot 4.

## Impact

No cambia la forma en que Zathuracode se ejecuta ni las variables Velocity del template. El impacto esta en el `pom.xml` generado: menos overrides, parent actualizado y dependencias externas revisadas para seguir compilando correctamente.
