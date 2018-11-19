import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IReservar } from 'app/shared/model/reservar.model';
import { Principal } from 'app/core';
import { ReservarService } from './reservar.service';

@Component({
    selector: 'jhi-reservar',
    templateUrl: './reservar.component.html'
})
export class ReservarComponent implements OnInit, OnDestroy {
    reservars: IReservar[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private reservarService: ReservarService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.reservarService.query().subscribe(
            (res: HttpResponse<IReservar[]>) => {
                this.reservars = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInReservars();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IReservar) {
        return item.id;
    }

    registerChangeInReservars() {
        this.eventSubscriber = this.eventManager.subscribe('reservarListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
