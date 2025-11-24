import { Injectable, Logger } from '@nestjs/common';
import { Kafka, Producer } from 'kafkajs';

const DEFAULT_BROKER = 'kafka:9092';
const DEFAULT_TOPIC = 'auth-login';

@Injectable()
export class KafkaProducerService {
  private readonly logger = new Logger(KafkaProducerService.name);
  private readonly topic = process.env.KAFKA_TOPIC_AUTH_LOGIN || DEFAULT_TOPIC;
  private producer?: Producer;
  private kafka = new Kafka({
    brokers: [process.env.KAFKA_BROKER || DEFAULT_BROKER],
    clientId: 'ms-auth',
  });

  private async getProducer(): Promise<Producer> {
    if (!this.producer) {
      this.producer = this.kafka.producer();
      try {
        await this.producer.connect();
        this.logger.log(`Kafka producer connected. Topic: ${this.topic}`);
      } catch (error) {
        this.logger.warn(`No se pudo conectar a Kafka: ${String(error)}`);
        throw error;
      }
    }
    return this.producer;
  }

  async emitLogin(payload: { email: string; estudianteId: number; loggedAt: string }) {
    try {
      const producer = await this.getProducer();
      await producer.send({
        topic: this.topic,
        messages: [{ value: JSON.stringify(payload) }],
      });
      this.logger.debug(`Evento de login enviado a Kafka para ${payload.email}`);
    } catch (error) {
      this.logger.warn(`No se pudo enviar evento de login a Kafka: ${String(error)}`);
    }
  }
}
