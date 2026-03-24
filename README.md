# Zathuracode

Generador de código interno para crear proyectos Spring Boot a partir de esquemas de base de datos relacionales.

Zathuracode toma una conexión JDBC, ejecuta reverse engineering sobre las tablas configuradas, construye metadata de entidades y genera un proyecto Java listo para compilar con una arquitectura base consistente. Actualmente el flujo está alineado con Java 25, Spring Boot 4.0.4, Hibernate Tools 7.3 y `jakarta.persistence`.

## Propósito

Este proyecto existe para acelerar la creación de servicios backend repetitivos dentro de la empresa, reduciendo el trabajo manual inicial y estandarizando:

- entidades JPA;
- repositories;
- DTOs y mappers;
- services;
- controllers REST;
- configuración base de seguridad;
- manejo de errores;
- archivos de build y soporte operativo;
- pruebas mínimas generadas.

El objetivo no es reemplazar el trabajo de diseño o negocio del equipo, sino entregar una base técnica sólida sobre la cual se continúa desarrollando.

## Qué genera

Por cada conjunto de tablas configuradas, Zathuracode genera un proyecto Spring Boot con una estructura como esta:

```text
src/main/java/<paquete-base>/
├── controller/
├── domain/
├── dto/
├── exception/
├── mapper/
├── repository/
├── security/
├── service/
├── utility/
└── SpringBootRunner.java
```

También genera:

- `pom.xml`
- `Dockerfile`
- `bitbucket-pipelines.yml`
- `src/main/resources/application.properties`
- `src/main/resources/application-dev.properties`
- `src/main/resources/META-INF/orm.xml`
- pruebas base en `src/test/java`

## Stack técnico

### Generador

- Java 25
- Maven
- Apache Velocity
- Hibernate Tools 7.3.0.Final
- Jakarta Persistence 3.2
- Log4j 2
- Google Java Format

### Proyecto generado

- Spring Boot 4.0.4
- Spring Data JPA
- Spring Security
- MapStruct
- Lombok
- JWT
- OpenAPI / Swagger

## Flujo de alto nivel

Zathuracode sigue este flujo:

1. Lee la configuración desde `zcode-gen.properties`.
2. Prepara paths y estructura Maven del proyecto destino.
3. Ejecuta reverse engineering con Hibernate Tools.
4. Genera entidades JPA temporales.
5. Carga metadata desde esas entidades usando `jakarta.persistence`.
6. Borra los `.class` temporales.
7. Ejecuta el generador SkyJet sobre la metadata.
8. Emite código fuente, configuración, seguridad, pruebas y archivos operativos.
9. Formatea el código generado.

El entrypoint oficial para ejecutar el proceso completo es:

```text
org.zcode.generator.ZcodeMain
```

## Arquitectura del repositorio

### Código fuente principal

- `src/main/java/org/zcode/generator`
  Contiene el entrypoint, factorías y utilidades del proceso de generación.

- `src/main/java/org/zcode/reverse`
  Contiene el pipeline de reverse engineering y la integración con Hibernate Tools.

- `src/main/java/org/zcode/metadata`
  Contiene la lectura y transformación de metadata a partir de las entidades temporales.

### Templates

- `templates/skyJet`
  Templates Velocity del proyecto final generado.

- `reverseTemplates`
  Templates usados por el proceso de reverse engineering.

### Configuración

- `config/`
  Archivos auxiliares del generador.

- `zcode-gen.properties`
  Configuración local de ejecución.

- `openspec/`
  Documentación de cambios, decisiones y evolución del proyecto mediante OpenSpec.

## Requisitos

Antes de ejecutar Zathuracode, asegúrate de tener:

- Java 25 instalado
- Maven 3.6.3 o superior
- acceso a la base de datos origen
- driver JDBC disponible vía Maven según el motor usado
- permisos de escritura sobre el directorio de salida

## Motores soportados

Actualmente el proyecto contempla generación desde:

- PostgreSQL
- MySQL
- Oracle
- SQL Server

Recomendación importante:

- Para MySQL moderno usa `com.mysql.cj.jdbc.Driver`.
- Evita commitear usuarios, contraseñas o URLs reales en `zcode-gen.properties`.

## Configuración de ejecución

El archivo `zcode-gen.properties` define el comportamiento del generador. Un ejemplo realista:

```properties
PROJECT_PATH=/Users/dgomez/demo-bank

GROUP_ID=com.vobi.demo
PROJECT_NAME=demo-bank
DOMAIN_PACKAGE_NAME=com.vobi.demo.domain

DRIVER_CLASS=org.postgresql.Driver
URL=jdbc:postgresql://127.0.0.1:5432/bank
USER=postgres
PASSWORD=postgres
SCHEMA=public
TABLE_LIST=document_type,customer,account,registered_account,transaction,user_type,users,transaction_type
```

### Propiedades principales

| Propiedad | Descripción |
|---|---|
| `PROJECT_PATH` | Ruta absoluta donde se generará el proyecto |
| `GROUP_ID` | `groupId` Maven del proyecto generado |
| `PROJECT_NAME` | Nombre del proyecto generado |
| `DOMAIN_PACKAGE_NAME` | Paquete donde se generan las entidades JPA |
| `DRIVER_CLASS` | Driver JDBC del motor origen |
| `URL` | URL JDBC de la base de datos |
| `USER` | Usuario de conexión |
| `PASSWORD` | Contraseña de conexión |
| `SCHEMA` | Esquema a inspeccionar |
| `CATALOG` | Catálogo, cuando el motor lo requiere |
| `TABLE_LIST` | Lista de tablas separadas por coma |

## Cómo ejecutar el generador

### 1. Compilar Zathuracode

```bash
mvn -DskipTests compile
```

### 2. Empaquetar

```bash
mvn -DskipTests package
```

### 3. Ejecutar el main

La forma recomendada en desarrollo es correr directamente la clase principal:

```bash
java -cp "target/classes:$(cat /tmp/zcode-main.classpath)" org.zcode.generator.ZcodeMain
```

Si necesitas reconstruir el classpath primero:

```bash
mvn -DskipTests dependency:build-classpath -Dmdep.outputFile=/tmp/zcode-main.classpath
```

## Resultado esperado

Si la ejecución termina correctamente, Zathuracode debe:

- conectarse a la base configurada;
- generar entidades temporales;
- construir metadata;
- crear el proyecto destino completo;
- finalizar con código de salida `0`.

En una ejecución exitosa verás en logs el cierre del generador SkyJet.

## Calidad del código generado

El generador fue modernizado para producir una base más alineada con buenas prácticas de Spring Boot 4.0.4.

### Mejoras incorporadas

- constructor injection en controllers y services;
- paquetes `controller` y `service` más simples;
- `404 Not Found` cuando un recurso no existe;
- `201 Created` en creación de recursos;
- `204 No Content` en eliminación;
- `ProblemDetail` para respuestas de error consistentes;
- configuración sensible externalizada a propiedades;
- CORS configurable;
- seguridad JWT sin secretos hardcodeados en clases Java;
- pruebas mínimas generadas:
  - smoke test de contexto/configuración;
  - test de mapper;
  - test de controller con `404`.

## Validación recomendada después de generar

Una vez creado el proyecto destino, valida al menos esto:

```bash
cd /ruta/al/proyecto-generado
mvn -DskipTests compile
mvn test-compile
mvn test
```

## OpenSpec

El proyecto usa OpenSpec para documentar decisiones, cambios y evolución técnica.

Los cambios activos e históricos se documentan en:

```text
openspec/changes/
```

Algunos cambios importantes ya documentados:

- modernización del flujo de generación;
- migración a Hibernate Tools 7.3;
- actualización del template a Spring Boot 4.0.4;
- mejora de calidad del código generado.

### Validar OpenSpec

```bash
OPENSPEC_TELEMETRY=0 openspec validate --changes
```

## Estructura de carpetas relevante

```text
.
├── config/
├── openspec/
├── reverseTemplates/
├── src/main/java/org/zcode/
│   ├── generator/
│   ├── metadata/
│   └── reverse/
├── templates/
│   └── skyJet/
└── zcode-gen.properties
```

## Buenas prácticas para el equipo

- Usa siempre `ZcodeMain` como entrypoint de referencia.
- No subas credenciales reales a Git.
- Valida el proyecto generado con Maven después de cada cambio importante del generador.
- Cuando cambies templates, reverse engineering o dependencias, actualiza también OpenSpec.
- Si la mejora afecta comportamiento funcional del generado, prueba con una base conocida antes de integrar.

## Troubleshooting

### El generador solo crea entidades y no termina el proyecto

Revisa:

- errores de template Velocity;
- problemas del formatter;
- fallos de reverse engineering;
- mensajes en logs alrededor de `SkyJet`.

### El proyecto generado no compila

Verifica:

- que `pom.xml.vm` esté alineado con el stack actual;
- imports generados;
- configuración de MapStruct y Lombok;
- compatibilidad del JDK usado para compilar.

### La conexión a la base falla

Comprueba:

- `DRIVER_CLASS`
- `URL`
- `USER`
- `PASSWORD`
- `SCHEMA`
- conectividad de red al motor origen

## Estado actual

A la fecha, Zathuracode:

- usa Hibernate Tools 7.3.0.Final;
- opera con `jakarta.persistence`;
- genera proyectos Spring Boot 4.0.4;
- incluye OpenSpec como mecanismo formal de evolución;
- genera código y pruebas base con una calidad inicial más sólida que las versiones anteriores.

## Próximos pasos sugeridos

Líneas de evolución razonables para el proyecto:

- enriquecer pruebas generadas;
- agregar más validaciones de contratos REST;
- mejorar observabilidad del proyecto generado;
- ampliar convenciones de seguridad por perfil;
- incorporar más estrategias de personalización del template.

## Licencia y uso

Proyecto de uso interno de la empresa.

Su objetivo es estandarizar y acelerar la generación de código base para equipos de desarrollo internos.
