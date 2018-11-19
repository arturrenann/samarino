import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReservar } from 'app/shared/model/reservar.model';

@Component({
    selector: 'jhi-reservar-detail',
    templateUrl: './reservar-detail.component.html'
})
export class ReservarDetailComponent implements OnInit {
    reservar: IReservar;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reservar }) => {
            this.reservar = reservar;
        });
    }

    previousState() {
        window.history.back();
    }
}
