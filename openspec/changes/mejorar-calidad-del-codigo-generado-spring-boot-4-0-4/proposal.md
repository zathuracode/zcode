## Why

El codigo generado por Zathuracode ya compila y arranca sobre Spring Boot 4.0.4, pero seguia saliendo con varios defaults inseguros o poco mantenibles: secretos embebidos, CORS demasiado abierto, respuestas HTTP poco expresivas, `findById` devolviendo `200` con `null`, manejo de errores ad hoc y ausencia total de pruebas generadas.

Eso obliga al equipo a rehacer manualmente una parte importante del proyecto cada vez que se genera una base nueva. El generador debe producir una base mas segura, mas consistente y mas alineada con practicas razonables de Spring Boot 4.0.4.

## What Changes

Este cambio mejora la calidad del codigo generado en los templates de SkyJet y en la logica del generador.

La actualizacion incluye:

- mover configuracion sensible a placeholders y propiedades de entorno;
- endurecer el flujo JWT y la configuracion CORS generada;
- modernizar controllers y services hacia constructor injection y respuestas HTTP mas correctas;
- generar `ProblemDetail` para errores comunes y `404` para recursos inexistentes;
- evitar defaults peligrosos como `@GeneratedValue` sobre llaves `String`;
- generar pruebas minimas para contexto, mappers y controllers;
- simplificar nombres de paquetes generados de `entity.controller` y `entity.service` a `controller` y `service`.

## Capabilities

### Added

- Capacidad para generar pruebas minimas listas para compilar y ejecutar.
- Capacidad para generar excepciones `404` explicitas con `ResourceNotFoundException`.
- Capacidad para generar configuracion de seguridad basada en propiedades externas.

### Modified

- Los controllers generados ahora devuelven respuestas HTTP mas expresivas.
- Los services generados usan constructor injection y eliminan `@Scope("singleton")`.
- El manejo de errores generado usa `ProblemDetail` y respuestas consistentes.

## Impact

No cambia la forma de invocar `ZcodeMain`, pero si cambia de forma visible el shape del proyecto generado: nuevos paquetes, nuevos tests, seguridad mas segura por defecto y endpoints CRUD con contratos mas correctos.
