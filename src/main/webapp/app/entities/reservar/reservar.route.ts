import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Reservar } from 'app/shared/model/reservar.model';
import { ReservarService } from './reservar.service';
import { ReservarComponent } from './reservar.component';
import { ReservarDetailComponent } from './reservar-detail.component';
import { ReservarUpdateComponent } from './reservar-update.component';
import { ReservarDeletePopupComponent } from './reservar-delete-dialog.component';
import { IReservar } from 'app/shared/model/reservar.model';

@Injectable({ providedIn: 'root' })
export class ReservarResolve implements Resolve<IReservar> {
    constructor(private service: ReservarService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((reservar: HttpResponse<Reservar>) => reservar.body));
        }
        return of(new Reservar());
    }
}

export const reservarRoute: Routes = [
    {
        path: 'reservar',
        component: ReservarComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reservars'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reservar/:id/view',
        component: ReservarDetailComponent,
        resolve: {
            reservar: ReservarResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reservars'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reservar/new',
        component: ReservarUpdateComponent,
        resolve: {
            reservar: ReservarResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reservars'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reservar/:id/edit',
        component: ReservarUpdateComponent,
        resolve: {
            reservar: ReservarResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reservars'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reservarPopupRoute: Routes = [
    {
        path: 'reservar/:id/delete',
        component: ReservarDeletePopupComponent,
        resolve: {
            reservar: ReservarResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reservars'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
