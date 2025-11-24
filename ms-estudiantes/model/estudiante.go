package model

import "time"

// Estudiante representa la entidad base del microservicio.
type Estudiante struct {
	ID              int        `json:"id"`
	Nombre          string     `json:"nombre"`
	Apellido        string     `json:"apellido"`
	DNI             string     `json:"dni"`
	Email           string     `json:"email"`
	Telefono        *string    `json:"telefono,omitempty"`
	FechaNacimiento string     `json:"fecha_nacimiento"`
	Direccion       *string    `json:"direccion,omitempty"`
	FechaRegistro   time.Time  `json:"fecha_registro"`
	Activo          bool       `json:"activo"`
}
