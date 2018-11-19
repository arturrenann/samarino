import { Moment } from 'moment';
import { IHotel } from 'app/shared/model//hotel.model';
import { ICliente } from 'app/shared/model//cliente.model';

export interface IReservar {
    id?: number;
    dias?: number;
    dataInicio?: Moment;
    dataEntrega?: Moment;
    valorTotReserva?: number;
    hoteis?: IHotel[];
    clientes?: ICliente[];
}

export class Reservar implements IReservar {
    constructor(
        public id?: number,
        public dias?: number,
        public dataInicio?: Moment,
        public dataEntrega?: Moment,
        public valorTotReserva?: number,
        public hoteis?: IHotel[],
        public clientes?: ICliente[]
    ) {}
}
