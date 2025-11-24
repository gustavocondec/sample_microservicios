import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AuthService } from './auth.service';
import { AuthController } from './auth.controller';
import { Estudiante } from './entities/estudiante.entity';
import { KafkaProducerService } from './kafka-producer.service';

@Module({
  imports: [TypeOrmModule.forFeature([Estudiante])],
  controllers: [AuthController],
  providers: [AuthService, KafkaProducerService],
})
export class AuthModule {}
