import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeagueStanding, getLeagueStandingIdentifier } from '../league-standing.model';

export type EntityResponseType = HttpResponse<ILeagueStanding>;
export type EntityArrayResponseType = HttpResponse<ILeagueStanding[]>;

@Injectable({ providedIn: 'root' })
export class LeagueStandingService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/league-standings');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(leagueStanding: ILeagueStanding): Observable<EntityResponseType> {
    return this.http.post<ILeagueStanding>(this.resourceUrl, leagueStanding, { observe: 'response' });
  }

  update(leagueStanding: ILeagueStanding): Observable<EntityResponseType> {
    return this.http.put<ILeagueStanding>(`${this.resourceUrl}/${getLeagueStandingIdentifier(leagueStanding) as number}`, leagueStanding, {
      observe: 'response',
    });
  }

  partialUpdate(leagueStanding: ILeagueStanding): Observable<EntityResponseType> {
    return this.http.patch<ILeagueStanding>(
      `${this.resourceUrl}/${getLeagueStandingIdentifier(leagueStanding) as number}`,
      leagueStanding,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeagueStanding>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeagueStanding[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLeagueStandingToCollectionIfMissing(
    leagueStandingCollection: ILeagueStanding[],
    ...leagueStandingsToCheck: (ILeagueStanding | null | undefined)[]
  ): ILeagueStanding[] {
    const leagueStandings: ILeagueStanding[] = leagueStandingsToCheck.filter(isPresent);
    if (leagueStandings.length > 0) {
      const leagueStandingCollectionIdentifiers = leagueStandingCollection.map(
        leagueStandingItem => getLeagueStandingIdentifier(leagueStandingItem)!
      );
      const leagueStandingsToAdd = leagueStandings.filter(leagueStandingItem => {
        const leagueStandingIdentifier = getLeagueStandingIdentifier(leagueStandingItem);
        if (leagueStandingIdentifier == null || leagueStandingCollectionIdentifiers.includes(leagueStandingIdentifier)) {
          return false;
        }
        leagueStandingCollectionIdentifiers.push(leagueStandingIdentifier);
        return true;
      });
      return [...leagueStandingsToAdd, ...leagueStandingCollection];
    }
    return leagueStandingCollection;
  }
}
