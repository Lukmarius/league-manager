import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeagueStanding, LeagueStanding } from '../league-standing.model';
import { LeagueStandingService } from '../service/league-standing.service';

@Injectable({ providedIn: 'root' })
export class LeagueStandingRoutingResolveService implements Resolve<ILeagueStanding> {
  constructor(protected service: LeagueStandingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeagueStanding> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leagueStanding: HttpResponse<LeagueStanding>) => {
          if (leagueStanding.body) {
            return of(leagueStanding.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeagueStanding());
  }
}
