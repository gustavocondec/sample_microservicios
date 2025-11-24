import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity({ name: 'estudiante' })
export class Estudiante {
  @PrimaryGeneratedColumn({ name: 'estudiante_id' })
  estudianteId!: number;

  @Column()
  nombre!: string;

  @Column()
  apellido!: string;

  @Column()
  dni!: string;

  @Column()
  email!: string;

  @Column({ nullable: true })
  telefono?: string;

  @Column({ name: 'fecha_nacimiento', type: 'date' })
  fechaNacimiento!: string;

  @Column({ nullable: true })
  direccion?: string;

  @Column({ name: 'fecha_registro', type: 'timestamp', default: () => 'CURRENT_TIMESTAMP' })
  fechaRegistro!: Date;

  @Column({ default: true })
  activo!: boolean;
}
