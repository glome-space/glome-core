import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S6WorkSharedModule } from '../shared';

import { TERM_ROUTE, TermComponent } from './';

@NgModule({
    imports: [
        S6WorkSharedModule,
        RouterModule.forRoot([ TERM_ROUTE ], { useHash: true })
    ],
    declarations: [
        TermComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S6WorkTermModule {}
