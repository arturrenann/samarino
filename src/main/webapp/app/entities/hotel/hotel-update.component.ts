import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IHotel } from 'app/shared/model/hotel.model';
import { HotelService } from './hotel.service';
import { IReservar } from 'app/shared/model/reservar.model';
import { ReservarService } from 'app/entities/reservar';

@Component({
    selector: 'jhi-hotel-update',
    templateUrl: './hotel-update.component.html'
})
export class HotelUpdateComponent implements OnInit {
    hotel: IHotel;
    isSaving: boolean;

    reservars: IReservar[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private hotelService: HotelService,
        private reservarService: ReservarService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ hotel }) => {
            this.hotel = hotel;
        });
        this.reservarService.query().subscribe(
            (res: HttpResponse<IReservar[]>) => {
                this.reservars = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.hotel.id !== undefined) {
            this.subscribeToSaveResponse(this.hotelService.update(this.hotel));
        } else {
            this.subscribeToSaveResponse(this.hotelService.create(this.hotel));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IHotel>>) {
        result.subscribe((res: HttpResponse<IHotel>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackReservarById(index: number, item: IReservar) {
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
