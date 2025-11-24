FROM golang:1.21-alpine AS builder
WORKDIR /app

COPY go.mod .
COPY . .

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o server .

FROM alpine:3.18
WORKDIR /app
ENV PORT=3002
COPY --from=builder /app/server ./server
COPY --from=builder /app/docs ./docs
EXPOSE 3002
CMD ["./server"]
