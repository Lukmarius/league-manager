import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMatchResult, MatchResult } from '../match-result.model';
import { MatchResultService } from '../service/match-result.service';

@Injectable({ providedIn: 'root' })
export class MatchResultRoutingResolveService implements Resolve<IMatchResult> {
  constructor(protected service: MatchResultService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMatchResult> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((matchResult: HttpResponse<MatchResult>) => {
          if (matchResult.body) {
            return of(matchResult.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MatchResult());
  }
}
