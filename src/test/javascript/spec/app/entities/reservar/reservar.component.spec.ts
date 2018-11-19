/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TrabalhoEsTestModule } from '../../../test.module';
import { ReservarComponent } from 'app/entities/reservar/reservar.component';
import { ReservarService } from 'app/entities/reservar/reservar.service';
import { Reservar } from 'app/shared/model/reservar.model';

describe('Component Tests', () => {
    describe('Reservar Management Component', () => {
        let comp: ReservarComponent;
        let fixture: ComponentFixture<ReservarComponent>;
        let service: ReservarService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TrabalhoEsTestModule],
                declarations: [ReservarComponent],
                providers: []
            })
                .overrideTemplate(ReservarComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReservarComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReservarService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Reservar(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.reservars[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
