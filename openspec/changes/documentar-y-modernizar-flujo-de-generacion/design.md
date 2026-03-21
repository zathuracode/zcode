# Design

## Overview

Zathuracode es un generador Java que parte de una base de datos relacional y produce una aplicacion Spring Boot a partir de metadata y templates Velocity. El flujo estable actual debe quedar documentado antes de seguir modernizando el stack porque algunas piezas son sensibles a cambios de version, especialmente `hibernate-tools`, el formatter y las plantillas de salida.

Este cambio define el flujo soportado como una cadena de etapas verificables:

1. `ZcodeMain` carga configuracion y prepara directorios de salida.
2. El reverse engineering consulta la base de datos y construye metadata.
3. La capa SkyJet transforma esa metadata en artefactos generados mediante templates.
4. El formatter aplica formato Java compatible con Java 25.
5. El proyecto generado debe seguir siendo compilable con su `pom.xml.vm` vigente.

## Goals

- Documentar el flujo real de generacion y sus fronteras de compatibilidad.
- Hacer explicitos los prerequisitos para ejecutar el generador con una base conocida.
- Estandarizar la validacion minima requerida cuando se cambia el flujo.
- Definir una politica de modernizacion incremental que preserve compatibilidad funcional.

## Non-Goals

- Migrar el generador a Hibernate 6.
- Migrar `javax.*` a `jakarta.*` dentro del generador.
- Reemplazar Hibernate Tools o Velocity por otra estrategia.
- Redisenar el proyecto generado.

## Current Flow

### 1. Configuracion y arranque

`ZcodeMain` toma configuracion del archivo local de generacion, resuelve la conexion JDBC, el esquema/tablas objetivo y la ruta de salida del proyecto generado.

### 2. Reverse engineering

Hibernate Tools inspecciona la base de datos y produce metadata relacional reutilizable por el pipeline de generacion. Esta etapa es especialmente sensible a upgrades de version y a cambios de dialecto o driver JDBC.

### 3. Metadata loading y templating

La metadata se transforma en estructuras consumibles por los templates SkyJet. Desde ahi se generan entidades, DTOs, repositories, mappers, services, controllers y archivos de soporte del proyecto Spring Boot.

### 4. Formatting

El formatter usa `google-java-format` en un proceso aislado para evitar conflictos de modulos con Java 25. El formateo debe mejorar salida, pero no puede impedir que el resto del proyecto se genere.

### 5. Packaging validation

Luego de generar codigo, el proyecto resultante debe seguir siendo compilable con el `pom.xml.vm` actual para considerarse una modernizacion segura.

## Modernization Policy

- Se permiten upgrades incrementales de dependencias estables si preservan el flujo end to end.
- Si una dependencia mas nueva rompe reverse engineering o generacion, se debe priorizar la version funcional y documentar el bloqueo para una fase posterior.
- Todo cambio en templates, formatter, dependencias de runtime o reverse engineering debe validarse ejecutando el generador contra una base conocida y compilando el proyecto generado.
- Las migraciones mayores a Jakarta o Hibernate 6 requieren propuesta separada.

## Validation Strategy

La validacion minima de este flujo queda definida como:

1. `mvn -DskipTests compile` en el generador.
2. `mvn -DskipTests package` en el generador.
3. Ejecucion directa de `org.zcode.generator.ZcodeMain`.
4. Confirmacion de generacion completa de entidades y capas de aplicacion.
5. `mvn -DskipTests compile` en el proyecto generado.

## Risks

- `hibernate-tools` sigue siendo el principal punto de compatibilidad fragil.
- Los cambios en templates pueden romper el proyecto generado sin romper el build del generador.
- Los cambios en formatter pueden detener el pipeline si no se degradan con seguridad.

## Rollout

Primero se documenta el flujo soportado y la politica de validacion. Sobre esa base, futuras propuestas podran modernizar dependencias o piezas aisladas del pipeline con un criterio uniforme de aceptacion.
