# 🧾 ClientMS – Microservicio de Clientes

## 📋 Descripción

Este microservicio gestiona operaciones CRUD sobre clientes, siguiendo una arquitectura contract-first basada en OpenAPI. Está construido con Spring Boot, MongoDB y herramientas de calidad integradas para asegurar consistencia y mantenibilidad del código.

---

## 🏗️ Arquitectura de Sistema

### 📊 Diagrama de Componentes
![Diagrama de Componentes](src/diagrams/Diagrama%20de%20componentes.png)

**Descripción:** Muestra la arquitectura de componentes del microservicio, incluyendo las capas de presentación (Controller), lógica de negocio (Service), acceso a datos (Repository) y las entidades de dominio. También representa las dependencias externas como MongoDB y las librerías de validación.

### 🔄 Diagrama de Secuencia
![Diagrama de Secuencia](src/diagrams/Diagra%20de%20secuencia.png)

**Descripción:** Ilustra el flujo de interacciones entre los diferentes componentes durante las operaciones CRUD principales. Muestra cómo las peticiones HTTP fluyen desde el controlador hasta la base de datos, incluyendo validaciones, transformaciones de datos y manejo de respuestas.

### 📈 Diagrama de Flujo
![Diagrama de Flujo](src/diagrams/Diagrama%20de%20flujo.png)

**Descripción:** Representa el flujo de decisión y proceso de negocio para las operaciones de cliente, incluyendo validaciones, verificaciones de duplicados y manejo de excepciones. Facilita la comprensión de la lógica de negocio implementada.

---

## 📘 Lista de Endpoints

| Método | Endpoint         | Descripción                         | Códigos de Estado |
|--------|------------------|-------------------------------------|-------------------|
| POST   | `/clientes`      | Registrar nuevo cliente             | 200, 400, 500    |
| GET    | `/clientes`      | Listar todos los clientes           | 200               |
| GET    | `/clientes/{id}` | Obtener cliente por ID              | 200, 404          |
| PUT    | `/clientes/{id}` | Actualizar cliente por ID           | 200, 400, 404, 409 |
| PATCH  | `/clientes/{id}` | Actualizar parcialmente cliente     | 200, 400, 404, 409 |
| DELETE | `/clientes/{id}` | Eliminar cliente por ID             | 204, 400, 404    |

---

## 📋 Reglas de Negocio

### ✅ Validaciones de Entrada
- **firstName**: Campo obligatorio, no puede estar vacío
- **lastName**: Campo obligatorio, no puede estar vacío  
- **dni**: Campo obligatorio para creación, debe ser único en el sistema
- **email**: Campo obligatorio, debe tener formato válido y ser único

### 🔄 Operaciones
- **Creación**: Todos los campos son obligatorios (firstName, lastName, dni, email)
- **Actualización (PUT)**: Requiere firstName, lastName y email (dni no se puede modificar)
- **Actualización (PATCH)**: Todos los campos son opcionales, al menos uno debe proporcionarse
- **Eliminación**: Solo requiere ID válido del cliente

### 🚫 Restricciones
- No se permite duplicación de email en el sistema
- El DNI es inmutable una vez creado el cliente
- Los IDs son generados automáticamente por el sistema

---

## 📚 Documentación

### 🔗 APIs y Especificaciones
- **OpenAPI Specification**: [`client-ms-openapi.yaml`](src/main/resources/openapi/client-ms-openapi.yaml)
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) (cuando el servicio esté ejecutándose)
- **API Docs JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### 📦 Colección Postman
- **Archivo**: [`clientms-collection.json`](postman/clientms-collection.json)
- **Importar en Postman**: Archivo > Import > Seleccionar archivo de colección
- **Variables de entorno**: Configurar `{{baseUrl}}` como `http://localhost:8080`

---

## 🧪 Tests y Calidad de Código

### ✅ Herramientas de Calidad Integradas

#### 🎨 Formatter (Google Java Style)
```bash
# Formatear código
mvn formatter:format

# Validar formato
mvn formatter:validate
```

#### 📏 Checkstyle (Validación de Estilo)
```bash
# Ejecutar checkstyle
mvn checkstyle:check

# Generar reporte
mvn checkstyle:checkstyle
```

#### 🧪 JaCoCo (Cobertura de Tests)
```bash
# Ejecutar tests con cobertura
mvn test

# Generar reporte de cobertura
mvn jacoco:report
```
**Reporte disponible en**: `target/site/jacoco/index.html`

### 📊 Métricas de Calidad
- **Cobertura de líneas**: >80%
- **Cobertura de ramas**: >70%
- **Reglas de Checkstyle**: Google Java Style Guide
- **Formato de código**: Google Java Format

---

## ✅ Checklist del Proyecto

### 🔧 Desarrollo
- [x] Estructura del proyecto configurada
- [x] Dependencias Maven configuradas
- [x] Configuración Spring Boot (application.yml)
- [x] Especificación OpenAPI definida
- [x] Modelos de datos (DTOs) implementados
- [x] Controladores REST implementados
- [x] Servicios de negocio implementados
- [x] Repositorios de datos configurados
- [x] Manejo de excepciones implementado

### 🧪 Testing
- [x] Tests unitarios de servicios
- [x] Tests de controladores
- [x] Tests de integración
- [x] Cobertura de tests >80%
- [x] Tests de validación de datos
- [x] Tests de manejo de errores

### 📋 Calidad de Código
- [x] Checkstyle configurado y passing
- [x] Formatter configurado (Google Style)
- [x] JaCoCo configurado para cobertura
- [x] Lombok para reducir boilerplate
- [x] Validaciones de entrada implementadas
- [x] Logging configurado

### 📚 Documentación
- [x] README completo y actualizado
- [x] Especificación OpenAPI actualizada
- [x] Swagger UI configurado
- [x] Colección Postman disponible
- [x] Diagramas de arquitectura incluidos
- [x] Comentarios en código cuando necesario

### 🚀 Despliegue
- [x] Configuración de profiles (dev, prod)
- [x] Dockerización (si aplicable)
- [x] Scripts de build automatizados
- [x] Pipeline CI/CD configurado
- [x] Variables de entorno documentadas

---

## 🚨 Checklist de Calidad (Pre-commit)

Antes de hacer commit o crear un pull request:

- [ ] Código formateado (`mvn formatter:format`)
- [ ] Reglas de estilo validadas (`mvn checkstyle:check`)
- [ ] Pruebas ejecutadas y pasando (`mvn test`)
- [ ] Cobertura generada (`mvn jacoco:report`)
- [ ] Documentación actualizada
- [ ] Commit claro y descriptivo
- [ ] Rama actualizada con `main`

---

## 🏷️ Badges

![Java](https://img.shields.io/badge/language-Java%2017-blue)
![Build](https://github.com/natalygiron/ClientMS/actions/workflows/maven.yml/badge.svg)
[![Swagger](https://img.shields.io/badge/docs-Swagger-blue?logo=swagger)](http://localhost:8080/swagger-ui/index.html)
