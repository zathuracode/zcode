## Why

El flujo de generacion de Zathuracode funciona, pero hoy depende demasiado de conocimiento tribal del equipo. Eso hace mas dificil incorporar personas nuevas, diagnosticar fallos en runtime y decidir hasta donde se puede modernizar el stack sin romper el reverse engineering ni los proyectos generados.

Los cambios recientes en compatibilidad con Java 25, formatter, dependencias y la migracion a Hibernate Tools 7.3/Jakarta dejaron claro que necesitamos una referencia explicita del flujo estable actual antes de abordar modernizaciones mayores como Hibernate 6 o una sustitucion completa del pipeline de reverse engineering.

## What Changes

Este cambio documenta y estandariza el flujo de generacion actual de Zathuracode, con enfasis en:

- describir el recorrido end to end desde `ZcodeMain` hasta el proyecto generado;
- dejar explicitas las etapas del flujo: reverse engineering, carga de metadatos, templating, formateo y empaquetado;
- dejar explicito que el flujo soportado actual usa Hibernate Tools 7.3, `jakarta.persistence` y compilacion temporal en proceso;
- documentar prerequisitos operativos y validaciones minimas para ejecutar el generador;
- establecer reglas para futuras modernizaciones incrementales sin romper compatibilidad del generador;
- exigir validacion end to end cuando se toquen dependencias, templates o el pipeline de generacion.

## Capabilities

### Added

- Capacidad para documentar el flujo de generacion soportado y sus prerequisitos operativos.
- Capacidad para definir una politica de modernizacion incremental con validacion end to end obligatoria.

## Impact

Este cambio no introduce una migracion tecnica grande por si mismo. Su impacto principal es:

- mejorar onboarding y soporte operativo del equipo;
- reducir regresiones al tocar formatter, dependencias, reverse engineering o templates;
- dejar una base clara para futuras propuestas de modernizacion del generador.
