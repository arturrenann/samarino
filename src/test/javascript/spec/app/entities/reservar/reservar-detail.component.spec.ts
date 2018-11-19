/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrabalhoEsTestModule } from '../../../test.module';
import { ReservarDetailComponent } from 'app/entities/reservar/reservar-detail.component';
import { Reservar } from 'app/shared/model/reservar.model';

describe('Component Tests', () => {
    describe('Reservar Management Detail Component', () => {
        let comp: ReservarDetailComponent;
        let fixture: ComponentFixture<ReservarDetailComponent>;
        const route = ({ data: of({ reservar: new Reservar(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TrabalhoEsTestModule],
                declarations: [ReservarDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReservarDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReservarDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.reservar).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
