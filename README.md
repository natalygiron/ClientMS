# 🧾 ClientMS – Microservicio de Clientes

Este microservicio gestiona operaciones CRUD sobre clientes, siguiendo una arquitectura contract-first basada en OpenAPI. Está construido con Spring Boot, MongoDB y herramientas de calidad integradas para asegurar consistencia y mantenibilidad.

---

## 🏷️ Badges

![Java](https://img.shields.io/badge/language-Java%2017-blue)
![Build](https://github.com/natalygiron/ClientMS/actions/workflows/maven.yml/badge.svg)
[![Swagger](https://img.shields.io/badge/docs-Swagger-blue?logo=swagger)](http://localhost:8080/swagger-ui/index.html)
<!-- ![License](https://img.shields.io/github/license/natalygiron/ClientMS) -->
---

## 📘 API Endpoints

| Método | Endpoint         | Descripción                         |
|--------|------------------|-------------------------------------|
| POST   | `/clientes`      | Registrar nuevo cliente             |
| GET    | `/clientes`      | Listar todos los clientes           |
| GET    | `/clientes/{id}` | Obtener cliente por ID              |
| PUT    | `/clientes/{id}` | Actualizar cliente por ID           |
| PATCH  | `/clientes/{id}` | Actualizar parcialmente cliente     |
| DELETE | `/clientes/{id}` | Eliminar cliente por ID             |

📎 Documentación interactiva: [Swagger UI](http://localhost:8080/swagger-ui/index.html)

---

## 🧪 Checklist de calidad

Antes de hacer commit o crear un pull request:

- [x] Código formateado (`mvn formatter:format`)
- [x] Reglas de estilo validadas (`mvn checkstyle:check`)
- [x] Pruebas ejecutadas (`mvn test`)
- [x] Cobertura generada (`mvn jacoco:report`)
- [x] Documentación actualizada (`client-ms-openapi.yaml`)
- [x] Commit claro y descriptivo
- [x] Pull request creado (no push directo a `main`)

---

## 📥 Pull Request Template

> Este repositorio requiere que todos los cambios pasen por revisión vía Pull Request.

```markdown
# 📦 Pull Request – ClientMS

## ✅ Descripción del cambio
<!-- Explica brevemente qué se implementa o corrige -->

## 🔍 Checklist de calidad
- [ ] Código formateado (`mvn formatter:format`)
- [ ] Reglas de estilo validadas (`mvn checkstyle:check`)
- [ ] Pruebas ejecutadas (`mvn test`)
- [ ] Cobertura generada (`mvn jacoco:report`)
- [ ] Documentación actualizada (`client-ms-openapi.yaml`)
- [ ] Commit claro y descriptivo
- [ ] Rama actualizada con `main`
- [ ] Revisión solicitada

## 📎 Referencias
<!-- Enlace a ticket, historia de usuario o documentación relacionada -->

## 👥 Revisor(es) sugerido(s)
<!-- Menciona a quien debería revisar este PR -->
```
---

## 🏗️ Arquitectura de sistema

### Diagrama de Componentes
![Diagrama de Componentes](src/diagrams/Diagrama%20de%20componentes.png)

### Diagrama de Secuencia
![Diagrama de Secuencia](src/diagrams/Diagra%20de%20secuencia.png)

### Diagrama de Flujo
![Diagrama de Flujo](src/diagrams/Diagrama%20de%20flujo.png)

---

## 📦 Estructura del proyecto
```código
client-ms/
├── src/
│   ├── main/
│   │   ├── java/com/bootcamp/clientms/
│   │   │   ├── controller/
│   │   │   ├── dto/request/
│   │   │   ├── dto/response/
│   │   │   ├── domain/
│   │   │   ├── service/
│   │   │   └── api/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── checkstyle.xml
│   │       └── openapi/client-ms-openapi.yaml
├── pom.xml
└── .github/workflows/maven.yml
```
---

## 🚨 Errores estándar de la API

| Código | Tipo de error        | Descripción breve                          | Recomendación para el cliente |
|--------|----------------------|--------------------------------------------|-------------------------------|
| 400    | Bad Request          | Datos inválidos o faltantes en la solicitud| Verifica campos requeridos y formato |
| 404    | Not Found            | Cliente no encontrado por ID               | Asegúrate de que el ID exista |
| 409    | Conflict             | Email o DNI ya registrado                  | Usa valores únicos o actualiza |
| 422    | Unprocessable Entity | Datos válidos pero no procesables          | Revisa reglas de negocio      |
| 500    | Internal Server Error| Error inesperado en el servidor            | Intenta nuevamente o contacta soporte |

📎 Todos los errores deben incluir un cuerpo JSON con estructura clara:

```json
{
  "timestamp": "2025-09-06T02:35:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Email must be valid",
  "path": "/clientes"
}
```
> Esta estructura facilita el debugging y permite que los clientes automaticen el manejo de errores.
