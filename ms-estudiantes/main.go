package main

import (
	"context"
	"log"
	"os"

	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"ms-estudiantes/controller"
	"ms-estudiantes/repository"
	"ms-estudiantes/service"
)

func main() {
	_ = loadEnv() // carga .env si existe, para desarrollo local

	ctx := context.Background()
	pool, err := newPool(ctx, getDatabaseURL())
	if err != nil {
		log.Fatalf("error creando pool: %v", err)
	}
	defer pool.Close()

	repo := repository.NewEstudianteRepository(pool)
	svc := service.NewEstudianteService(repo)
	ctrl := controller.NewEstudianteController(svc)

	r := gin.Default()
	ctrl.RegisterRoutes(r)

	port := os.Getenv("PORT")
	if port == "" {
		port = "3002"
	}
	log.Printf("ms-estudiantes escuchando en :%s", port)
	if err := r.Run(":" + port); err != nil {
		log.Fatalf("servidor caído: %v", err)
	}
}

func newPool(ctx context.Context, url string) (*pgxpool.Pool, error) {
	cfg, err := pgxpool.ParseConfig(url)
	if err != nil {
		return nil, err
	}
	cfg.ConnConfig.DefaultQueryExecMode = pgx.QueryExecModeSimpleProtocol
	return pgxpool.NewWithConfig(ctx, cfg)
}

func getDatabaseURL() string {
	if url := os.Getenv("DATABASE_URL"); url != "" {
		return url
	}
	host := os.Getenv("DB_HOST")
	if host == "" {
		host = "postgres"
	}
	port := os.Getenv("DB_PORT")
	if port == "" {
		port = "5432"
	}
	name := os.Getenv("DB_NAME")
	if name == "" {
		name = "universidad_db"
	}
	user := os.Getenv("DB_USER")
	if user == "" {
		user = "univ_user"
	}
	pass := os.Getenv("DB_PASSWORD")
	if pass == "" {
		pass = "univ_pass"
	}
	return "postgres://" + user + ":" + pass + "@" + host + ":" + port + "/" + name
}

func loadEnv() error {
	if _, err := os.Stat(".env"); err == nil {
		return godotenv.Load()
	}
	return nil
}
