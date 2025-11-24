import { Injectable, UnauthorizedException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { LoginDto } from './dto/login.dto';
import { Estudiante } from './entities/estudiante.entity';

@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(Estudiante)
    private readonly estudianteRepo: Repository<Estudiante>,
  ) {}

  async login(dto: LoginDto) {
    const estudiante = await this.estudianteRepo.findOne({
      where: { email: dto.email, dni: dto.dni, activo: true },
    });
    if (!estudiante) {
      throw new UnauthorizedException('Credenciales inválidas');
    }
    const token = Buffer.from(`${estudiante.email}:${estudiante.estudianteId}`).toString('base64');
    return {
      token,
      estudiante: {
        id: estudiante.estudianteId,
        nombre: estudiante.nombre,
        apellido: estudiante.apellido,
        email: estudiante.email,
      },
    };
  }
}
