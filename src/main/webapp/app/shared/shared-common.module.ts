import { NgModule } from '@angular/core';

import { TrabalhoEsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [TrabalhoEsSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [TrabalhoEsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class TrabalhoEsSharedCommonModule {}
