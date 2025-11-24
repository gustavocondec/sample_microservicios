CREATE TABLE profesor (
    profesor_id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    especialidad VARCHAR(100),
    titulo_academico VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE facultad (
    facultad_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT,
    ubicacion VARCHAR(100),
    decano VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE carrera (
    carrera_id SERIAL PRIMARY KEY,
    facultad_id INTEGER NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    duracion_semestres INTEGER NOT NULL,
    titulo_otorgado VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_facultad FOREIGN KEY (facultad_id) REFERENCES facultad(facultad_id) ON DELETE RESTRICT,
    CONSTRAINT uk_carrera_nombre UNIQUE (nombre)
);

CREATE TABLE curso (
    curso_id SERIAL PRIMARY KEY,
    carrera_id INTEGER NOT NULL,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    creditos INTEGER NOT NULL,
    nivel_semestre INTEGER NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_carrera FOREIGN KEY (carrera_id) REFERENCES carrera(carrera_id) ON DELETE RESTRICT,
    CONSTRAINT ck_creditos CHECK (creditos > 0),
    CONSTRAINT ck_nivel_semestre CHECK (nivel_semestre > 0)
);

CREATE TABLE seccion (
    seccion_id SERIAL PRIMARY KEY,
    curso_id INTEGER NOT NULL,
    profesor_id INTEGER NOT NULL,
    codigo VARCHAR(20) NOT NULL,
    capacidad_maxima INTEGER NOT NULL,
    aula VARCHAR(50),
    horario VARCHAR(50),
    dias VARCHAR(50),
    periodo_academico VARCHAR(20) NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_curso FOREIGN KEY (curso_id) REFERENCES curso(curso_id) ON DELETE RESTRICT,
    CONSTRAINT fk_profesor FOREIGN KEY (profesor_id) REFERENCES profesor(profesor_id) ON DELETE RESTRICT,
    CONSTRAINT uk_seccion_periodo UNIQUE (curso_id, codigo, periodo_academico),
    CONSTRAINT ck_capacidad_maxima CHECK (capacidad_maxima > 0)
);

INSERT INTO profesor (nombre, apellido, dni, email, telefono, especialidad, titulo_academico)
VALUES
    ('María', 'Rodríguez', '11112222', 'maria.rodriguez@example.com', '+51 900000101', 'Matemáticas', 'Magíster en Educación'),
    ('Carlos', 'Fernández', '22223333', 'carlos.fernandez@example.com', '+51 900000102', 'Programación', 'Ingeniero de Sistemas');

INSERT INTO facultad (nombre, descripcion, ubicacion, decano)
VALUES
    ('Ingeniería', 'Facultad de Ingeniería y Tecnología', 'Campus Central', 'Dr. Julio Paredes');

INSERT INTO carrera (facultad_id, nombre, descripcion, duracion_semestres, titulo_otorgado)
VALUES
    (1, 'Ingeniería de Sistemas', 'Carrera orientada al desarrollo de software y sistemas', 10, 'Ingeniero de Sistemas');

INSERT INTO curso (carrera_id, codigo, nombre, descripcion, creditos, nivel_semestre)
VALUES
    (1, 'SIS101', 'Introducción a Programación', 'Fundamentos de programación', 4, 1),
    (1, 'SIS201', 'Estructuras de Datos', 'Listas, colas, pilas, árboles', 4, 3);

INSERT INTO seccion (curso_id, profesor_id, codigo, capacidad_maxima, aula, horario, dias, periodo_academico, fecha_inicio, fecha_fin)
VALUES
    (1, 2, 'A1', 35, 'Aula 201', '08:00-10:00', 'Lun-Mie', '2024-I', '2024-03-01', '2024-07-15'),
    (2, 1, 'B1', 30, 'Aula 305', '10:00-12:00', 'Mar-Jue', '2024-I', '2024-03-02', '2024-07-16');
