import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeagueStandingComponent } from '../list/league-standing.component';
import { LeagueStandingDetailComponent } from '../detail/league-standing-detail.component';
import { LeagueStandingUpdateComponent } from '../update/league-standing-update.component';
import { LeagueStandingRoutingResolveService } from './league-standing-routing-resolve.service';

const leagueStandingRoute: Routes = [
  {
    path: '',
    component: LeagueStandingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeagueStandingDetailComponent,
    resolve: {
      leagueStanding: LeagueStandingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeagueStandingUpdateComponent,
    resolve: {
      leagueStanding: LeagueStandingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeagueStandingUpdateComponent,
    resolve: {
      leagueStanding: LeagueStandingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leagueStandingRoute)],
  exports: [RouterModule],
})
export class LeagueStandingRoutingModule {}
