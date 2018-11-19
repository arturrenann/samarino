import { IReservar } from 'app/shared/model//reservar.model';

export interface ICliente {
    id?: number;
    nome?: string;
    identidade?: string;
    cpf?: string;
    email?: string;
    telefone?: string;
    reservas?: IReservar[];
}

export class Cliente implements ICliente {
    constructor(
        public id?: number,
        public nome?: string,
        public identidade?: string,
        public cpf?: string,
        public email?: string,
        public telefone?: string,
        public reservas?: IReservar[]
    ) {}
}
