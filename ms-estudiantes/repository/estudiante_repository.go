package repository

import (
	"context"
	"errors"

	"github.com/jackc/pgx/v5/pgxpool"
	"ms-estudiantes/model"
)

var ErrNotFound = errors.New("estudiante no encontrado")

type EstudianteRepository struct {
	pool *pgxpool.Pool
}

func NewEstudianteRepository(pool *pgxpool.Pool) *EstudianteRepository {
	return &EstudianteRepository{pool: pool}
}

func (r *EstudianteRepository) List(ctx context.Context) ([]model.Estudiante, error) {
	rows, err := r.pool.Query(ctx, baseSelect+" ORDER BY estudiante_id")
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var result []model.Estudiante
	for rows.Next() {
		var e model.Estudiante
		if err := rows.Scan(
			&e.ID, &e.Nombre, &e.Apellido, &e.DNI, &e.Email, &e.Telefono,
			&e.FechaNacimiento, &e.Direccion, &e.FechaRegistro, &e.Activo,
		); err != nil {
			return nil, err
		}
		result = append(result, e)
	}
	return result, nil
}

func (r *EstudianteRepository) Get(ctx context.Context, id int) (model.Estudiante, error) {
	var e model.Estudiante
	err := r.pool.QueryRow(ctx, baseSelect+" WHERE estudiante_id=$1", id).
		Scan(&e.ID, &e.Nombre, &e.Apellido, &e.DNI, &e.Email, &e.Telefono, &e.FechaNacimiento, &e.Direccion, &e.FechaRegistro, &e.Activo)
	if err != nil {
		return model.Estudiante{}, ErrNotFound
	}
	return e, nil
}

func (r *EstudianteRepository) Create(ctx context.Context, e model.Estudiante) (model.Estudiante, error) {
	err := r.pool.QueryRow(ctx, `
		INSERT INTO estudiante (nombre, apellido, dni, email, telefono, fecha_nacimiento, direccion, activo)
		VALUES ($1,$2,$3,$4,$5,$6,$7,$8)
		RETURNING estudiante_id, fecha_registro
	`, e.Nombre, e.Apellido, e.DNI, e.Email, e.Telefono, e.FechaNacimiento, e.Direccion, true).
		Scan(&e.ID, &e.FechaRegistro)
	if err != nil {
		return model.Estudiante{}, err
	}
	e.Activo = true
	return e, nil
}

func (r *EstudianteRepository) Update(ctx context.Context, id int, e model.Estudiante) (model.Estudiante, error) {
	cmd, err := r.pool.Exec(ctx, `
		UPDATE estudiante SET nombre=$1, apellido=$2, dni=$3, email=$4, telefono=$5, fecha_nacimiento=$6, direccion=$7, activo=$8
		WHERE estudiante_id=$9
	`, e.Nombre, e.Apellido, e.DNI, e.Email, e.Telefono, e.FechaNacimiento, e.Direccion, e.Activo, id)
	if err != nil {
		return model.Estudiante{}, err
	}
	if cmd.RowsAffected() == 0 {
		return model.Estudiante{}, ErrNotFound
	}
	e.ID = id
	return e, nil
}

func (r *EstudianteRepository) Delete(ctx context.Context, id int) error {
	cmd, err := r.pool.Exec(ctx, `DELETE FROM estudiante WHERE estudiante_id=$1`, id)
	if err != nil {
		return err
	}
	if cmd.RowsAffected() == 0 {
		return ErrNotFound
	}
	return nil
}

const baseSelect = `
SELECT estudiante_id, nombre, apellido, dni, email, telefono, fecha_nacimiento, direccion, fecha_registro, activo
FROM estudiante
`
