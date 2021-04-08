import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeagueComponent } from '../list/league.component';
import { LeagueDetailComponent } from '../detail/league-detail.component';
import { LeagueUpdateComponent } from '../update/league-update.component';
import { LeagueRoutingResolveService } from './league-routing-resolve.service';

const leagueRoute: Routes = [
  {
    path: '',
    component: LeagueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeagueDetailComponent,
    resolve: {
      league: LeagueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeagueUpdateComponent,
    resolve: {
      league: LeagueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeagueUpdateComponent,
    resolve: {
      league: LeagueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leagueRoute)],
  exports: [RouterModule],
})
export class LeagueRoutingModule {}
