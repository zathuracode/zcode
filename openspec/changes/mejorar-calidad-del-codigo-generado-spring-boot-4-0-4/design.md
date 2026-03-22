# Design

## Overview

La mejora se implementa en los templates de SkyJet y en `SkyJet.java`, no como correcciones manuales sobre un proyecto generado. La estrategia fue tocar primero los defaults operativos y despues la forma del codigo generado para que la mejora se replique en cualquier proyecto futuro.

## Secure Defaults

- `application.properties` importa `.env` de forma opcional.
- `application-dev.properties` deja de tener credenciales reales y usa placeholders.
- JWT deja de depender de secretos hardcodeados en clases Java.
- CORS usa una lista configurable de origenes permitidos y no queda abierto globalmente.

## Service and Controller Shape

- Controllers generados se mueven a `controller`.
- Services generados se mueven a `service`.
- La inyeccion por campo se reemplaza por constructor injection con Lombok.
- `findById` usa `ResourceNotFoundException` y devuelve `404` cuando no existe el recurso.
- `save` devuelve `201 Created` con `Location`.
- `delete` devuelve `204 No Content`.

## Error Handling

- `GeneralExceptionHandler` usa `ProblemDetail`.
- Se estandarizan respuestas para validacion, payload invalido, errores de negocio y recursos no encontrados.

## Generated Tests

- Se agregan tres pruebas base:
  - smoke test de contexto web/configuracion con `WebApplicationContextRunner`;
  - test de mapper;
  - test de controller con `MockMvc` standalone.
- Las pruebas evitan depender de una base de datos real para ser estables en CI y en entornos locales.

## Validation

Este cambio queda aceptado si:

1. El generador compila.
2. `org.zcode.generator.ZcodeMain` genera correctamente `demo-bank`.
3. El proyecto generado pasa `mvn -DskipTests compile`.
4. El proyecto generado pasa `mvn test-compile`.
5. El proyecto generado pasa `mvn test`.

## Residual Risks

- Mockito emite warnings por auto-attachment en Java 25, aunque no bloquea el build.
- El stack generado sigue siendo CRUD generico; mejoras mas profundas de API, paginacion y observabilidad pueden evolucionarse en cambios posteriores.
