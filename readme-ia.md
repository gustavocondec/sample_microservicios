## Orquestación Docker para microservicios

- Compose con Postgres + 4 microservicios (`ms-auth`, `ms-estudiantes`, `ms-facultad`, `ms-matricula`), cada uno con su Dockerfile base generado.
- Puertos expuestos: ms-auth 3001, ms-estudiantes 3002, ms-facultad 3003, ms-matricula 3004. Postgres 5432.
- Credenciales BD (configuradas en `docker-compose.yml` y heredadas a todos los servicios):
  - DB_HOST=postgres, DB_PORT=5432, DB_NAME=universidad_db, DB_USER=univ_user, DB_PASSWORD=univ_pass
  - DATABASE_URL=postgres://univ_user:univ_pass@postgres:5432/universidad_db
- Inicialización de BD separada por servicio en `docker/initdb/`:
  - `01-ms-estudiantes.sql`: tabla `estudiante` + 2 registros de ejemplo.
  - `02-ms-facultad.sql`: tablas `profesor`, `facultad`, `carrera`, `curso`, `seccion` + datos de ejemplo coherentes.
  - `03-ms-matricula.sql`: tabla `matricula` + 2 registros que referencian estudiantes y secciones insertados antes.

### Supuestos en Dockerfiles
- `ms-auth/Dockerfile` (NestJS con TypeORM): espera scripts `npm run build` y `dist/main`. Ajustar si usas otro entrypoint.
- `ms-estudiantes/Dockerfile` (Go): compila `./...` y genera binario `server`. Cambia la ruta del paquete principal si tu `main` está en otra carpeta.
- `ms-facultad` y `ms-matricula` (Spring Boot): construyen con Maven y copian `target/*.jar` a `app.jar`. Ajusta si el nombre del artefacto o la estructura difiere.

### Cómo levantar
1. Completar el código real en cada carpeta (`ms-auth/`, `ms-estudiantes/`, `ms-facultad/`, `ms-matricula/`) respetando los supuestos o adaptando los Dockerfiles.
2. Construir y levantar: `docker-compose up -d --build`
3. Verificar contenedores: `docker ps` y healthcheck de Postgres (el resto depende de tus endpoints).

### Variables de entorno por servicio
- `ms-auth/.env.example`: PORT, DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD (o `DATABASE_URL`; `.env` se carga al iniciar Nest).
- `ms-estudiantes/.env.example`: PORT, DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD (o `DATABASE_URL`; `.env` se carga si existe).
- `ms-facultad/.env.example` y `ms-matricula/.env.example`: PORT, DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD. Spring Boot toma estos valores de entorno (usa defaults si faltan).

## Microservicios implementados (skeleton listo)

- `ms-auth` (NestJS + TypeORM): `/ms-auth/login` acepta `{ email, dni }`, valida contra tabla `estudiante` y retorna token básico + datos del estudiante. Config DB por env (`DB_HOST`, etc.). Build con `npm run build`.
- `ms-estudiantes` (Go + Gin + pgx): rutas bajo `/ms-estudiantes/estudiantes` con CRUD completo. Usa `DATABASE_URL` o variables DB_* (opcional carga `.env` local). Estructura en capas controller→service→repository→model. Binario `server` compilado en Docker. Docs en `/ms-estudiantes/api-docs` (spec en `/ms-estudiantes/api-docs/openapi.json`).
- `ms-facultad` (Spring Boot): contexto `/ms-facultad`. CRUD para `facultades`, `carreras`, `cursos`, `secciones` en repos `facultad`, `curso`, `seccion` (resto de tablas presentes para FK). Config DB en `application.properties`.
- `ms-matricula` (Spring Boot): contexto `/ms-matricula`. CRUD en `/matriculas`, valida unicidad estudiante+sección, asigna estado/fecha por defecto si faltan. Config DB en `application.properties`.

### Endpoints de referencia
- Auth: `POST /ms-auth/login`
- Estudiantes: `GET/POST /ms-estudiantes/estudiantes`, `GET/PUT/DELETE /ms-estudiantes/estudiantes/{id}`
- Facultad: `GET/POST /ms-facultad/facultades`, `GET/PUT/DELETE /ms-facultad/facultades/{id}`
- Carrera: `GET/POST /ms-facultad/carreras`, `GET/PUT/DELETE /ms-facultad/carreras/{id}`
- Curso: `GET/POST /ms-facultad/cursos`, `GET/PUT/DELETE /ms-facultad/cursos/{id}`
- Sección: `GET/POST /ms-facultad/secciones`, `GET/PUT/DELETE /ms-facultad/secciones/{id}`
- Matrícula: `GET/POST /ms-matricula/matriculas`, `GET/PUT/DELETE /ms-matricula/matriculas/{id}`

### Documentación de APIs
- `ms-auth`: Swagger UI en `/ms-auth/api-docs`. Ejemplo de login (seed): `{ "email": "ana.perez@example.com", "dni": "12345678" }`.
- `ms-estudiantes`: Swagger UI en `/ms-estudiantes/api-docs`, spec en `/ms-estudiantes/api-docs/openapi.json`.
- `ms-facultad` y `ms-matricula`: Swagger UI vía Springdoc en `/ms-facultad/api-docs` y `/ms-matricula/api-docs`; specs en `/ms-facultad/v3/api-docs` y `/ms-matricula/v3/api-docs`.
