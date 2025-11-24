package controller

import "github.com/gin-gonic/gin"

func respondJSON(ctx *gin.Context, status int, data interface{}) {
	ctx.JSON(status, data)
}

func respondError(ctx *gin.Context, status int, msg string) {
	ctx.JSON(status, gin.H{"error": msg})
}
