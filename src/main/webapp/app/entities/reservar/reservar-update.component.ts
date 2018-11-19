import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IReservar } from 'app/shared/model/reservar.model';
import { ReservarService } from './reservar.service';
import { IHotel } from 'app/shared/model/hotel.model';
import { HotelService } from 'app/entities/hotel';
import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from 'app/entities/cliente';

@Component({
    selector: 'jhi-reservar-update',
    templateUrl: './reservar-update.component.html'
})
export class ReservarUpdateComponent implements OnInit {
    reservar: IReservar;
    isSaving: boolean;

    hotels: IHotel[];

    clientes: ICliente[];
    dataInicioDp: any;
    dataEntregaDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private reservarService: ReservarService,
        private hotelService: HotelService,
        private clienteService: ClienteService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reservar }) => {
            this.reservar = reservar;
            this.reservar.valorTotReserva = this.calcularValorTot();
        });
        this.hotelService.query().subscribe(
            (res: HttpResponse<IHotel[]>) => {
                this.hotels = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.clienteService.query().subscribe(
            (res: HttpResponse<ICliente[]>) => {
                this.clientes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.reservar.id !== undefined && this.reservar.hoteis[0].qtdAcomodacoes !== 0) {
            this.subscribeToSaveResponse(this.reservarService.update(this.reservar));
        } else {
            this.reservar.valorTotReserva = this.calcularValorTot();
            this.reservar.hoteis[0].qtdAcomodacoes = this.reservar.hoteis[0].qtdAcomodacoes - 1;
            this.subscribeToSaveResponse(this.reservarService.create(this.reservar));
        }
    }

    calcularValorTot() {
        const tot = this.reservar.dias * this.reservar.hoteis[0].valorDiaria;
        return tot;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IReservar>>) {
        result.subscribe((res: HttpResponse<IReservar>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackHotelById(index: number, item: IHotel) {
        return item.id;
    }

    trackClienteById(index: number, item: ICliente) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
