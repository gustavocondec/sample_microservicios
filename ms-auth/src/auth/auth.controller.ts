import { Controller, Post, Body } from '@nestjs/common';
import { ApiBody, ApiTags, ApiOkResponse } from '@nestjs/swagger';
import { AuthService } from './auth.service';
import { LoginDto } from './dto/login.dto';
import { LoginResponseDto } from './dto/login-response.dto';

@ApiTags('auth')
@Controller()
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('login')
  @ApiBody({
    description: 'Credenciales del estudiante (usar datos seed para probar el login)',
    examples: {
      anaPerez: {
        summary: 'Ana Pérez (seed)',
        value: {
          email: 'ana.perez@example.com',
          dni: '12345678',
        },
      },
      luisGarcia: {
        summary: 'Luis García (seed)',
        value: {
          email: 'luis.garcia@example.com',
          dni: '87654321',
        },
      },
    },
  })
  @ApiOkResponse({ type: LoginResponseDto })
  login(@Body() dto: LoginDto) {
    return this.authService.login(dto);
  }
}
