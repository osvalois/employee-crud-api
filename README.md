# Employee Management API

## Índice
1. [Descripción General](#descripción-general)
2. [Características Clave](#características-clave)
3. [Arquitectura](#arquitectura)
4. [Tecnologías y Frameworks](#tecnologías-y-frameworks)
5. [Estructura del Proyecto](#estructura-del-proyecto)
6. [Configuración y Despliegue](#configuración-y-despliegue)
   - [Requisitos Previos](#requisitos-previos)
   - [Configuración del Entorno](#configuración-del-entorno)
   - [Compilación](#compilación)
   - [Ejecución](#ejecución)
   - [Despliegue con Docker](#despliegue-con-docker)
7. [API Documentation](#api-documentation)
   - [Endpoints](#endpoints)
   - [Modelos de Datos](#modelos-de-datos)
   - [Ejemplos de Uso](#ejemplos-de-uso)
8. [Seguridad](#seguridad)
   - [Autenticación y Autorización](#autenticación-y-autorización)
   - [Manejo de Datos Sensibles](#manejo-de-datos-sensibles)
   - [Mejores Prácticas Implementadas](#mejores-prácticas-implementadas)
9. [Manejo de Errores y Excepciones](#manejo-de-errores-y-excepciones)
10. [Patrones de Resiliencia](#patrones-de-resiliencia)
11. [Monitoreo y Observabilidad](#monitoreo-y-observabilidad)
12. [Pruebas](#pruebas)
    - [Pruebas Unitarias](#pruebas-unitarias)
    - [Pruebas de Integración](#pruebas-de-integración)
    - [Pruebas de Rendimiento](#pruebas-de-rendimiento)
13. [Escalabilidad y Rendimiento](#escalabilidad-y-rendimiento)
14. [Integración Continua y Despliegue Continuo (CI/CD)](#integración-continua-y-despliegue-continuo-cicd)
15. [Contribución](#contribución)
16. [Versionado](#versionado)
17. [Licencia](#licencia)
18. [Contacto y Soporte](#contacto-y-soporte)
19. [Agradecimientos](#agradecimientos)
20. [Changelog](#changelog)

## Descripción General

La Employee Management API es una solución de clase mundial diseñada para proporcionar una gestión robusta, escalable y segura de datos de empleados. Utilizando tecnologías de vanguardia y siguiendo las mejores prácticas de la industria, esta API ofrece un conjunto completo de operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para la gestión de empleados, así como funcionalidades avanzadas como búsqueda, promoción y análisis estadístico.

Desarrollada con un enfoque en la programación reactiva, alta disponibilidad y resiliencia, esta API está diseñada para manejar cargas de trabajo intensivas y proporcionar tiempos de respuesta óptimos incluso en condiciones de alta concurrencia.

## Características Clave

- **Operaciones CRUD Reactivas**: Implementación completa de operaciones CRUD utilizando programación reactiva para una gestión eficiente de los recursos del sistema.
- **Búsqueda Avanzada**: Capacidades de búsqueda flexibles y eficientes utilizando MongoDB.
- **Gestión de Promociones**: Funcionalidad para manejar promociones de empleados, incluyendo actualización de posición y salario.
- **Análisis Estadístico**: Endpoints para obtener información estadística como los empleados mejor pagados y rangos salariales.
- **Seguridad Robusta**: Implementación de Spring Security con autenticación basada en roles y JWT.
- **Documentación Interactiva**: Integración de Swagger/OpenAPI para una documentación de API interactiva y fácil de usar.
- **Manejo de Errores Consistente**: Sistema global de manejo de excepciones para proporcionar respuestas de error coherentes y significativas.
- **Resiliencia y Tolerancia a Fallos**: Implementación de patrones de resiliencia utilizando Resilience4j.
- **Monitoreo y Métricas**: Integración de Spring Actuator para monitoreo de salud y métricas detalladas.
- **Pruebas Exhaustivas**: Cobertura completa de pruebas unitarias, de integración y de rendimiento.
- **Despliegue Containerizado**: Soporte para despliegue con Docker y Docker Compose.

## Arquitectura

La Employee Management API sigue una arquitectura de microservicios, diseñada para ser modular, escalable y fácilmente mantenible. Los principales componentes de la arquitectura son:

1. **Capa de Presentación (API Layer)**:
   - Controladores REST que manejan las solicitudes HTTP.
   - Implementación de OpenAPI para documentación.

2. **Capa de Lógica de Negocio (Service Layer)**:
   - Servicios que encapsulan la lógica de negocio.
   - Implementación de patrones de resiliencia.

3. **Capa de Acceso a Datos (Repository Layer)**:
   - Repositorios reactivos para interactuar con MongoDB.

4. **Capa de Persistencia**:
   - MongoDB como base de datos principal.

5. **Componentes Transversales**:
   - Configuración de seguridad.
   - Manejo global de excepciones.
   - Monitoreo y métricas.

Esta arquitectura permite una separación clara de responsabilidades, facilitando el mantenimiento y la evolución del sistema.

## Tecnologías y Frameworks

- **Java 17**: Última versión LTS de Java, aprovechando las nuevas características del lenguaje.
- **Spring Boot 3.2.3**: Framework para crear aplicaciones Spring stand-alone.
- **Spring WebFlux**: Para construir aplicaciones web reactivas.
- **Spring Data MongoDB Reactive**: Para interacción reactiva con MongoDB.
- **MongoDB**: Base de datos NoSQL para almacenamiento de datos.
- **Swagger/OpenAPI**: Para documentación de API interactiva.
- **MapStruct**: Para mapeo eficiente entre objetos.
- **Resilience4j**: Biblioteca para implementar patrones de resiliencia.
- **Spring Security**: Para autenticación y autorización.
- **JUnit 5**: Framework de pruebas unitarias.
- **Mockito**: Framework de mocking para pruebas.
- **Docker**: Para containerización de la aplicación.
- **Maven**: Herramienta de gestión y construcción de proyectos.

## Estructura del Proyecto

```
employee-crud-api/
│
├── src/
│   ├── main/
│   │   ├── java/com/picura/employee/
│   │   │   ├── config/
│   │   │   │   ├── MongoConfig.java
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   ├── ResilienceConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   └── EmployeeController.java
│   │   │   ├── dto/
│   │   │   │   └── EmployeeDTO.java
│   │   │   ├── exception/
│   │   │   │   ├── EmployeeNotFoundException.java
│   │   │   │   ├── ErrorDetails.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── mapper/
│   │   │   │   └── EmployeeMapper.java
│   │   │   ├── model/
│   │   │   │   └── Employee.java
│   │   │   ├── repository/
│   │   │   │   └── EmployeeRepository.java
│   │   │   ├── service/
│   │   │   │   └── EmployeeService.java
│   │   │   └── EmployeeApplication.java
│   │   └── resources/
│   │       └── application.yml
│   │
│   └── test/
│       └── java/com/picura/employee/
│           ├── controller/
│           ├── service/
│           └── repository/
│
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

Esta estructura organiza el código de manera lógica y modular, facilitando la navegación y el mantenimiento del proyecto.

## Configuración y Despliegue

### Requisitos Previos

- JDK 17
- Maven 3.6+
- MongoDB 4.4+
- Docker y Docker Compose (para despliegue containerizado)

### Configuración del Entorno

1. Clonar el repositorio:
   ```
   git clone https://github.com/osvalois/employee-crud-api.git
   ```

2. Navegar al directorio del proyecto:
   ```
   cd employee-crud-api
   ```

3. Configurar las variables de entorno:
   Crear un archivo `.env` en la raíz del proyecto con las siguientes variables:
   ```
   MONGO_URI=mongodb+srv://your_username:your_password@your_cluster.mongodb.net/your_database
   JWT_SECRET=your_jwt_secret
   ```

### Compilación

Compilar el proyecto usando Maven:

```
mvn clean install
```

Este comando compilará el código, ejecutará las pruebas y generará el archivo JAR en el directorio `target/`.

### Ejecución

Para ejecutar la aplicación localmente:

```
java -jar target/employee-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en `http://localhost:8080`.

### Despliegue con Docker

1. Construir la imagen Docker:
   ```
   docker build -t employee-api .
   ```

2. Ejecutar el contenedor:
   ```
   docker run -p 8080:8080 --env-file .env employee-api
   ```

Para desplegar la aplicación junto con una instancia de MongoDB usando Docker Compose:

```
docker-compose up -d
```

Este comando iniciará tanto la aplicación como MongoDB en contenedores separados.

## API Documentation

La documentación detallada de la API está disponible a través de Swagger UI en `http://localhost:8080/swagger-ui.html`.

### Endpoints

| Método HTTP | Endpoint                           | Descripción                                     |
|-------------|------------------------------------|-------------------------------------------------|
| GET         | /api/v1/employees                  | Obtener todos los empleados                     |
| GET         | /api/v1/employees/{id}             | Obtener un empleado por ID                      |
| POST        | /api/v1/employees                  | Crear un nuevo empleado                         |
| PUT         | /api/v1/employees/{id}             | Actualizar un empleado existente                |
| DELETE      | /api/v1/employees/{id}             | Eliminar un empleado                            |
| GET         | /api/v1/employees/search           | Buscar empleados por nombre                     |
| PUT         | /api/v1/employees/{id}/promote     | Promover a un empleado                          |
| GET         | /api/v1/employees/top-earners      | Obtener los empleados mejor pagados             |
| GET         | /api/v1/employees/salary-extremes  | Obtener empleados con salario mínimo y máximo   |
| GET         | /api/v1/employees/recent-hires     | Obtener contrataciones recientes                |

### Modelos de Datos

#### EmployeeDTO

```json
{
  "id": "string",
  "nombre": "string",
  "puesto": "string",
  "salario": 0,
  "fechaContratacion": "2024-01-01"
}
```

### Ejemplos de Uso

#### Crear un Nuevo Empleado

Request:
```http
POST /api/v1/employees
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "puesto": "Desarrollador Senior",
  "salario": 75000,
  "fechaContratacion": "2024-01-15"
}
```

Response:
```json
{
  "id": "5f07c259-b35a-4729-a8b9-8f12a3f28a25",
  "nombre": "Juan Pérez",
  "puesto": "Desarrollador Senior",
  "salario": 75000,
  "fechaContratacion": "2024-01-15"
}
```

## Seguridad

### Autenticación y Autorización

La API utiliza Spring Security con JWT (JSON Web Tokens) para la autenticación y autorización. Cada endpoint está protegido y requiere los roles adecuados para acceder.

#### Roles de Usuario

- `ROLE_ADMIN`: Acceso completo a todas las funcionalidades.
- `ROLE_HR`: Acceso a la mayoría de las funcionalidades, excepto operaciones críticas.
- `ROLE_EMPLOYEE`: Acceso limitado, principalmente a su propia información.

### Manejo de Datos Sensibles

- Los datos sensibles como contraseñas se almacenan utilizando algoritmos de hash seguros.
- La información personal de los empleados se encripta en la base de datos.

### Mejores Prácticas Implementadas

- Uso de HTTPS para todas las comunicaciones.
- Implementación de rate limiting para prevenir ataques de fuerza bruta.
- Validación y sanitización de todas las entradas de usuario.
- Principio de mínimo privilegio en la asignación de roles.

## Manejo de Errores y Excepciones

Se implementa un sistema global de manejo de excepciones para proporcionar respuestas de error consistentes y significativas. Todos los errores se registran para su análisis posterior.

Ejemplo de respuesta de error:

```json
{
  "timestamp": "2024-01-15T12:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 5f07c259-b35a-4729-a8b9-8f12a3f28a25",
  "path": "/api/v1/employees/5f07c259-b35a-4729-a8b9-8f12a3f28a25"
}
```

## Patrones de Resiliencia

Se utilizan varios patrones de resiliencia implementados con Resilience4j:

- **Circuit Breaker**: Previene llamadas a sistemas que están fallando.
- **Retry**: Reintenta operaciones que pueden fallar debido a problemas temporales.
- **Rate Limiter**: Limita el número de llamadas a un servicio en un período de tiempo.
- **Bulkhead**: Aísla diferentes partes del sistema para prevenir fallos en cascada.

Configuración de ejemplo para Circuit Breaker:

```yaml
resilience4j