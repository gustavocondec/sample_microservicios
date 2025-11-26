# Orquestacion de microservicios (Docker Compose)

Repositorio con 5 servicios y Postgres listos para levantarse via `docker-compose`. Incluye seeds SQL, ejemplos de variables de entorno y rutas de API/Swagger para que cualquiera pueda probar el entorno local.

## Servicios y puertos

| Servicio             | Tecnologia                  | Puerto host -> contenedor | Base path / Swagger                      | Notas breves |
|----------------------|-----------------------------|---------------------------|------------------------------------------|--------------|
| Postgres             | Postgres 15                 | 5432 -> 5432              | n/a                                      | Se inicializa con seeds en `docker/initdb`. Volumen `db_data`. |
| ms-auth              | NestJS + TypeORM            | 3001 -> 3001              | `/ms-auth` / `/ms-auth/api-docs`         | Login de estudiantes contra Postgres. Emite evento a Kafka. |
| ms-estudiantes       | Go + Gin + pgx              | 3002 -> 3002              | `/ms-estudiantes` / `/ms-estudiantes/api-docs` | CRUD de estudiantes. Sirve Swagger estatico. |
| ms-facultad          | Spring Boot                 | 3003 -> 3003              | `/ms-facultad` / `/ms-facultad/api-docs` | CRUD de facultades, carreras, cursos y secciones. |
| ms-matricula         | Spring Boot                 | 3004 -> 3004              | `/ms-matricula` / `/ms-matricula/api-docs` | CRUD de matriculas. POST protegido con token emitido por ms-auth. |
| Kafka + Zookeeper    | Confluent 7.5               | 9092, 2181                | n/a                                      | Topico `auth-login` se crea auto. |
| ms-notifications     | Spring Boot                 | 3005 -> 3005              | n/a (no expone HTTP)                     | Consumer de Kafka; envia email via Resend. |

## Variables de entorno

Compose ya inyecta valores por defecto; solo necesitas modificarlos si vas a apuntar a otra BD, cambiar puertos o usar credenciales reales.

- Postgres (se comparten con los servicios):  
  `DB_HOST=postgres`, `DB_PORT=5432`, `DB_NAME=universidad_db`, `DB_USER=univ_user`, `DB_PASSWORD=univ_pass`, `DATABASE_URL=postgres://univ_user:univ_pass@postgres:5432/universidad_db`
- Kafka (ms-auth productor y ms-notifications consumidor):  
  `KAFKA_BROKER=kafka:9092`, `KAFKA_TOPIC_AUTH_LOGIN=auth-login`, `KAFKA_CONSUMER_GROUP=ms-notifications`
- Correo (ms-notifications):  
  `RESEND_API_KEY` (obligatorio en prod; en Compose hay un valor de prueba), `RESEND_FROM=no-reply@resend.dev`
- Puertos de app (cada servicio): `PORT=3001|3002|3003|3004|3005`

Archivos `.env.example` en `ms-auth/`, `ms-estudiantes/`, `ms-facultad/` y `ms-matricula/` sirven para correrlos fuera de Docker (`cp .env.example .env` y ajusta).

## Seeds de base de datos (cargan al levantar Postgres)

Ubicacion: `docker/initdb/`
- `01-ms-estudiantes.sql`: tabla `estudiante` con dos registros (Ana Perez, Luis Garcia).
- `02-ms-facultad.sql`: tablas `profesor`, `facultad`, `carrera`, `curso`, `seccion` con datos de ejemplo coherentes.
- `03-ms-matricula.sql`: tabla `matricula` con dos registros vinculados a los seeds previos.

## Como levantar con Docker Compose

1) Requisitos: Docker y Docker Compose (v2+).  
2) (Opcional) Ajusta variables en `docker-compose.yml` o agrega un `.env` para `RESEND_API_KEY` real.  
3) Build y arranque:
```bash
docker-compose up -d --build
```
4) Revisa estado:
```bash
docker ps
# Postgres debe estar healthy antes de que arranquen los servicios que dependen de el.
```
5) Logs (ejemplos):
```bash
docker logs -f ms-auth
docker logs -f ms-notifications
```
6) Detener y limpiar (incluye volumen de datos):
```bash
docker-compose down -v
```

## Endpoints y Swagger

- ms-auth (3001):  
  - `POST http://localhost:3001/ms-auth/login` (Swagger: `/ms-auth/api-docs`).  
  - Body de ejemplo (seed): `{"email":"ana.perez@example.com","dni":"12345678"}`.  
  - Respuesta incluye token base64 `email:estudianteId` usado por ms-matricula.

- ms-estudiantes (3002):  
  - Base: `http://localhost:3002/ms-estudiantes`.  
  - CRUD: `/estudiantes`, `/estudiantes/{id}`.  
  - Swagger estatico: `http://localhost:3002/ms-estudiantes/api-docs` (spec en `ms-estudiantes/docs/openapi.json`).

- ms-facultad (3003):  
  - Base: `http://localhost:3003/ms-facultad`.  
  - CRUD: `/facultades`, `/carreras`, `/cursos`, `/secciones`.  
  - Swagger: `http://localhost:3003/ms-facultad/api-docs`.

- ms-matricula (3004):  
  - Base: `http://localhost:3004/ms-matricula`.  
  - CRUD: `/matriculas`, `/matriculas/{id}`.  
  - `POST /matriculas` requiere header `Authorization: Bearer <token ms-auth>`.  
  - Swagger: `http://localhost:3004/ms-matricula/api-docs` (incluye esquema bearer base64).

- ms-notifications (3005):  
  - No expone API HTTP. Escucha el topico `auth-login` en Kafka y envia correos via Resend.

## Flujo propuesto para probar rapido

1) Hacer login con un estudiante seed en ms-auth para obtener token:  
   `curl -X POST http://localhost:3001/ms-auth/login -H "Content-Type: application/json" -d '{"email":"ana.perez@example.com","dni":"12345678"}'`
2) Usar el token devuelto en el `Authorization` Bearer para crear una matricula en ms-matricula:  
   `curl -X POST http://localhost:3004/ms-matricula/matriculas -H "Authorization: Bearer <token>" -H "Content-Type: application/json" -d '{"estudianteId":1,"seccionId":2,"estado":"PAGADO","costo":1200,"metodoPago":"TARJETA"}'`
3) Verificar que ms-notifications reciba el evento de login y (si configuraste `RESEND_API_KEY` real) envie el correo. Revisa `docker logs -f ms-notifications`.

## Desarrollo local sin Docker (opcional)

- Clona el repo, copia `.env.example` -> `.env` en cada servicio y ajusta credenciales.  
- Sube Postgres local con las credenciales/DB anteriores y ejecuta los scripts de `docker/initdb` en orden si necesitas datos.  
- Ejecuta cada servicio segun su tecnologia:
  - ms-auth: `npm install && npm run start:dev`
  - ms-estudiantes: `go run ./...`
  - ms-facultad y ms-matricula: `mvn spring-boot:run`
  - ms-notifications: `mvn spring-boot:run`

## Notas adicionales

- El volumen `db_data` conserva datos entre reinicios. Usa `docker-compose down -v` para reiniciar la BD desde cero y volver a cargar seeds.  
- Kafka crea automaticamente el topico `auth-login` (habilitado `auto.create.topics.enable=true`).  
- ms-notifications trae un `RESEND_API_KEY` de prueba en `docker-compose.yml`; cambia a tu clave real antes de usarlo en serio.
