package service

import (
	"context"
	"errors"

	"ms-estudiantes/model"
	"ms-estudiantes/repository"
)

var ErrValidation = errors.New("datos inválidos")

type EstudianteService struct {
	repo *repository.EstudianteRepository
}

func NewEstudianteService(repo *repository.EstudianteRepository) *EstudianteService {
	return &EstudianteService{repo: repo}
}

func (s *EstudianteService) List(ctx context.Context) ([]model.Estudiante, error) {
	return s.repo.List(ctx)
}

func (s *EstudianteService) Get(ctx context.Context, id int) (model.Estudiante, error) {
	return s.repo.Get(ctx, id)
}

func (s *EstudianteService) Create(ctx context.Context, e model.Estudiante) (model.Estudiante, error) {
	if err := validateEstudiante(e); err != nil {
		return model.Estudiante{}, err
	}
	return s.repo.Create(ctx, e)
}

func (s *EstudianteService) Update(ctx context.Context, id int, e model.Estudiante) (model.Estudiante, error) {
	if err := validateEstudiante(e); err != nil {
		return model.Estudiante{}, err
	}
	return s.repo.Update(ctx, id, e)
}

func (s *EstudianteService) Delete(ctx context.Context, id int) error {
	return s.repo.Delete(ctx, id)
}

func validateEstudiante(e model.Estudiante) error {
	if e.Nombre == "" || e.Apellido == "" || e.DNI == "" || e.Email == "" || e.FechaNacimiento == "" {
		return ErrValidation
	}
	return nil
}

// CreateOrUpdateEstudiante es el payload que se mapea a la entidad de dominio.
type CreateOrUpdateEstudiante struct {
	Nombre          string  `json:"nombre"`
	Apellido        string  `json:"apellido"`
	DNI             string  `json:"dni"`
	Email           string  `json:"email"`
	Telefono        *string `json:"telefono"`
	FechaNacimiento string  `json:"fecha_nacimiento"`
	Direccion       *string `json:"direccion"`
	Activo          bool    `json:"activo"`
}

func (p CreateOrUpdateEstudiante) ToModel() model.Estudiante {
	return model.Estudiante{
		Nombre:          p.Nombre,
		Apellido:        p.Apellido,
		DNI:             p.DNI,
		Email:           p.Email,
		Telefono:        p.Telefono,
		FechaNacimiento: p.FechaNacimiento,
		Direccion:       p.Direccion,
		Activo:          p.Activo,
	}
}
