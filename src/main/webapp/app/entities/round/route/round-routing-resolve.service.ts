import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRound, Round } from '../round.model';
import { RoundService } from '../service/round.service';

@Injectable({ providedIn: 'root' })
export class RoundRoutingResolveService implements Resolve<IRound> {
  constructor(protected service: RoundService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRound> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((round: HttpResponse<Round>) => {
          if (round.body) {
            return of(round.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Round());
  }
}
