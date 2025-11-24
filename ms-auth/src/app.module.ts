import { Module } from "@nestjs/common";
import { TypeOrmModule } from "@nestjs/typeorm";
import { AuthModule } from "./auth/auth.module";
import { Estudiante } from "./auth/entities/estudiante.entity";

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: "postgres",
      host: process.env.DB_HOST || "postgres",
      port: Number(process.env.DB_PORT) || 5432,
      username: process.env.DB_USER || "univ_user",
      password: process.env.DB_PASSWORD || "univ_pass",
      database: process.env.DB_NAME || "universidad_db",
      entities: [Estudiante],
      synchronize: false,
    }),
    AuthModule,
  ],
})
export class AppModule {}
