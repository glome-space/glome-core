import { Route } from '@angular/router';

import { UserRouteAccessService } from '../shared';
import { TermComponent } from './';

export const TERM_ROUTE: Route = {
    path: 'jhi-term',
    component: TermComponent,
    data: {
        pageTitle: 'Term View'
    }
};
