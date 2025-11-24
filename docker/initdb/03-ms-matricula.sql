CREATE TABLE matricula (
    matricula_id SERIAL PRIMARY KEY,
    estudiante_id INTEGER NOT NULL,
    seccion_id INTEGER NOT NULL,
    fecha_matricula DATE NOT NULL DEFAULT CURRENT_DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    costo NUMERIC(10, 2) NOT NULL,
    metodo_pago VARCHAR(50),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiante(estudiante_id) ON DELETE RESTRICT,
    CONSTRAINT fk_seccion FOREIGN KEY (seccion_id) REFERENCES seccion(seccion_id) ON DELETE RESTRICT,
    CONSTRAINT uk_matricula_seccion UNIQUE (estudiante_id, seccion_id),
    CONSTRAINT ck_estado CHECK (estado IN ('PENDIENTE', 'PAGADO', 'ANULADO', 'COMPLETADO')),
    CONSTRAINT ck_costo CHECK (costo >= 0)
);

INSERT INTO matricula (estudiante_id, seccion_id, fecha_matricula, estado, costo, metodo_pago)
VALUES
    (1, 1, '2024-03-05', 'PAGADO', 1200.00, 'TARJETA'),
    (2, 2, '2024-03-06', 'PENDIENTE', 1100.00, 'EFECTIVO');
