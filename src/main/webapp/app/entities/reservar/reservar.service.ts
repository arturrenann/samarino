import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReservar } from 'app/shared/model/reservar.model';

type EntityResponseType = HttpResponse<IReservar>;
type EntityArrayResponseType = HttpResponse<IReservar[]>;

@Injectable({ providedIn: 'root' })
export class ReservarService {
    public resourceUrl = SERVER_API_URL + 'api/reservars';

    constructor(private http: HttpClient) {}

    create(reservar: IReservar): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(reservar);
        return this.http
            .post<IReservar>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(reservar: IReservar): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(reservar);
        return this.http
            .put<IReservar>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IReservar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReservar[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(reservar: IReservar): IReservar {
        const copy: IReservar = Object.assign({}, reservar, {
            dataInicio: reservar.dataInicio != null && reservar.dataInicio.isValid() ? reservar.dataInicio.format(DATE_FORMAT) : null,
            dataEntrega: reservar.dataEntrega != null && reservar.dataEntrega.isValid() ? reservar.dataEntrega.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dataInicio = res.body.dataInicio != null ? moment(res.body.dataInicio) : null;
        res.body.dataEntrega = res.body.dataEntrega != null ? moment(res.body.dataEntrega) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((reservar: IReservar) => {
            reservar.dataInicio = reservar.dataInicio != null ? moment(reservar.dataInicio) : null;
            reservar.dataEntrega = reservar.dataEntrega != null ? moment(reservar.dataEntrega) : null;
        });
        return res;
    }
}
