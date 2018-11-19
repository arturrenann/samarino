/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { ReservarService } from 'app/entities/reservar/reservar.service';
import { IReservar, Reservar } from 'app/shared/model/reservar.model';

describe('Service Tests', () => {
    describe('Reservar Service', () => {
        let injector: TestBed;
        let service: ReservarService;
        let httpMock: HttpTestingController;
        let elemDefault: IReservar;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ReservarService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Reservar(0, 0, currentDate, currentDate, 0);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        dataInicio: currentDate.format(DATE_FORMAT),
                        dataEntrega: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Reservar', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        dataInicio: currentDate.format(DATE_FORMAT),
                        dataEntrega: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dataInicio: currentDate,
                        dataEntrega: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Reservar(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Reservar', async () => {
                const returnedFromService = Object.assign(
                    {
                        dias: 1,
                        dataInicio: currentDate.format(DATE_FORMAT),
                        dataEntrega: currentDate.format(DATE_FORMAT),
                        valorTotReserva: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        dataInicio: currentDate,
                        dataEntrega: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Reservar', async () => {
                const returnedFromService = Object.assign(
                    {
                        dias: 1,
                        dataInicio: currentDate.format(DATE_FORMAT),
                        dataEntrega: currentDate.format(DATE_FORMAT),
                        valorTotReserva: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dataInicio: currentDate,
                        dataEntrega: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(take(1), map(resp => resp.body))
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Reservar', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
