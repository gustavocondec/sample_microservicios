import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AuthService } from './auth.service';
import { AuthController } from './auth.controller';
import { Estudiante } from './entities/estudiante.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Estudiante])],
  controllers: [AuthController],
  providers: [AuthService],
})
export class AuthModule {}
