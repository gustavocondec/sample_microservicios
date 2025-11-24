package controller

import (
	"errors"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"ms-estudiantes/repository"
	"ms-estudiantes/service"
)

type EstudianteController struct {
	service *service.EstudianteService
}

func NewEstudianteController(service *service.EstudianteService) *EstudianteController {
	return &EstudianteController{service: service}
}

func (c *EstudianteController) RegisterRoutes(r *gin.Engine) {
	g := r.Group("/ms-estudiantes")
	g.GET("/estudiantes", c.list)
	g.POST("/estudiantes", c.create)
	g.GET("/estudiantes/:id", c.get)
	g.PUT("/estudiantes/:id", c.update)
	g.DELETE("/estudiantes/:id", c.delete)
	g.Static("/api-docs", "./docs")
}

func (c *EstudianteController) list(ctx *gin.Context) {
	result, err := c.service.List(ctx.Request.Context())
	if err != nil {
		respondError(ctx, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(ctx, http.StatusOK, result)
}

func (c *EstudianteController) get(ctx *gin.Context) {
	id, err := parseID(ctx)
	if err != nil {
		respondError(ctx, http.StatusBadRequest, "id inválido")
		return
	}
	e, err := c.service.Get(ctx.Request.Context(), id)
	if err != nil {
		if errors.Is(err, repository.ErrNotFound) {
			respondError(ctx, http.StatusNotFound, err.Error())
			return
		}
		respondError(ctx, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(ctx, http.StatusOK, e)
}

func (c *EstudianteController) create(ctx *gin.Context) {
	var payload service.CreateOrUpdateEstudiante
	if err := ctx.ShouldBindJSON(&payload); err != nil {
		respondError(ctx, http.StatusBadRequest, "body inválido")
		return
	}
	e, err := c.service.Create(ctx.Request.Context(), payload.ToModel())
	if err != nil {
		switch {
		case errors.Is(err, service.ErrValidation):
			respondError(ctx, http.StatusBadRequest, err.Error())
		default:
			respondError(ctx, http.StatusInternalServerError, err.Error())
		}
		return
	}
	respondJSON(ctx, http.StatusCreated, e)
}

func (c *EstudianteController) update(ctx *gin.Context) {
	id, err := parseID(ctx)
	if err != nil {
		respondError(ctx, http.StatusBadRequest, "id inválido")
		return
	}
	var payload service.CreateOrUpdateEstudiante
	if err := ctx.ShouldBindJSON(&payload); err != nil {
		respondError(ctx, http.StatusBadRequest, "body inválido")
		return
	}
	e, err := c.service.Update(ctx.Request.Context(), id, payload.ToModel())
	if err != nil {
		switch {
		case errors.Is(err, service.ErrValidation):
			respondError(ctx, http.StatusBadRequest, err.Error())
		case errors.Is(err, repository.ErrNotFound):
			respondError(ctx, http.StatusNotFound, err.Error())
		default:
			respondError(ctx, http.StatusInternalServerError, err.Error())
		}
		return
	}
	respondJSON(ctx, http.StatusOK, e)
}

func (c *EstudianteController) delete(ctx *gin.Context) {
	id, err := parseID(ctx)
	if err != nil {
		respondError(ctx, http.StatusBadRequest, "id inválido")
		return
	}
	if err := c.service.Delete(ctx.Request.Context(), id); err != nil {
		if errors.Is(err, repository.ErrNotFound) {
			respondError(ctx, http.StatusNotFound, err.Error())
			return
		}
		respondError(ctx, http.StatusInternalServerError, err.Error())
		return
	}
	ctx.Status(http.StatusNoContent)
}

func parseID(ctx *gin.Context) (int, error) {
	idStr := ctx.Param("id")
	return strconv.Atoi(idStr)
}
