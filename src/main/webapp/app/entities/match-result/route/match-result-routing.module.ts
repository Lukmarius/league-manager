import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatchResultComponent } from '../list/match-result.component';
import { MatchResultDetailComponent } from '../detail/match-result-detail.component';
import { MatchResultUpdateComponent } from '../update/match-result-update.component';
import { MatchResultRoutingResolveService } from './match-result-routing-resolve.service';

const matchResultRoute: Routes = [
  {
    path: '',
    component: MatchResultComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MatchResultDetailComponent,
    resolve: {
      matchResult: MatchResultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MatchResultUpdateComponent,
    resolve: {
      matchResult: MatchResultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MatchResultUpdateComponent,
    resolve: {
      matchResult: MatchResultRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(matchResultRoute)],
  exports: [RouterModule],
})
export class MatchResultRoutingModule {}
