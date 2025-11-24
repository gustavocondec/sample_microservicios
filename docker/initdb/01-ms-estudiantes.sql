CREATE TABLE estudiante (
    estudiante_id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

INSERT INTO estudiante (nombre, apellido, dni, email, telefono, fecha_nacimiento, direccion)
VALUES
    ('Ana', 'Pérez', '12345678', 'ana.perez@example.com', '+51 900000001', '2000-05-12', 'Av. Siempre Viva 123'),
    ('Luis', 'García', '87654321', 'luis.garcia@example.com', '+51 900000002', '1999-08-20', 'Jr. Los Sauces 456');
