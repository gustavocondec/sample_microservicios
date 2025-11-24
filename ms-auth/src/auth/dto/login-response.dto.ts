import { ApiProperty } from '@nestjs/swagger';

export class LoginResponseDto {
  @ApiProperty({ example: 'YW5hLnBlcmV6QGV4YW1wbGUuY29tOjE=' })
  token: string;

  @ApiProperty({
    example: {
      id: 1,
      nombre: 'Ana',
      apellido: 'Pérez',
      email: 'ana.perez@example.com',
    },
  })
  estudiante: {
    id: number;
    nombre: string;
    apellido: string;
    email: string;
  };
}
