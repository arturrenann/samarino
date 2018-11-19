import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TrabalhoEsClienteModule } from './cliente/cliente.module';
import { TrabalhoEsHotelModule } from './hotel/hotel.module';
import { TrabalhoEsReservarModule } from './reservar/reservar.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        TrabalhoEsClienteModule,
        TrabalhoEsHotelModule,
        TrabalhoEsReservarModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TrabalhoEsEntityModule {}
