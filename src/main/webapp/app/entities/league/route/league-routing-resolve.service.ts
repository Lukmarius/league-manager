import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeague, League } from '../league.model';
import { LeagueService } from '../service/league.service';

@Injectable({ providedIn: 'root' })
export class LeagueRoutingResolveService implements Resolve<ILeague> {
  constructor(protected service: LeagueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeague> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((league: HttpResponse<League>) => {
          if (league.body) {
            return of(league.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new League());
  }
}
