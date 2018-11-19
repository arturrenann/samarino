import { IReservar } from 'app/shared/model//reservar.model';

export interface IHotel {
    id?: number;
    nome?: string;
    localizacao?: string;
    cep?: string;
    qtdAcomodacoes?: number;
    valorDiaria?: number;
    reservars?: IReservar[];
}

export class Hotel implements IHotel {
    constructor(
        public id?: number,
        public nome?: string,
        public localizacao?: string,
        public cep?: string,
        public qtdAcomodacoes?: number,
        public valorDiaria?: number,
        public reservars?: IReservar[]
    ) {}
}
