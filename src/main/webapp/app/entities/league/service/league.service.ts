import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeague, getLeagueIdentifier } from '../league.model';
import { ILeagueRequest } from 'app/entities/league/league-request.model';

export type EntityResponseType = HttpResponse<ILeague>;
export type EntityArrayResponseType = HttpResponse<ILeague[]>;

@Injectable({ providedIn: 'root' })
export class LeagueService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/leagues');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(league: ILeagueRequest): Observable<EntityResponseType> {
    return this.http.post<ILeague>(`${this.resourceUrl}/create`, league, { observe: 'response' });
  }

  update(league: ILeague): Observable<EntityResponseType> {
    return this.http.put<ILeague>(`${this.resourceUrl}/${getLeagueIdentifier(league) as number}`, league, { observe: 'response' });
  }

  partialUpdate(league: ILeague): Observable<EntityResponseType> {
    return this.http.patch<ILeague>(`${this.resourceUrl}/${getLeagueIdentifier(league) as number}`, league, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeague>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeague[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLeagueToCollectionIfMissing(leagueCollection: ILeague[], ...leaguesToCheck: (ILeague | null | undefined)[]): ILeague[] {
    const leagues: ILeague[] = leaguesToCheck.filter(isPresent);
    if (leagues.length > 0) {
      const leagueCollectionIdentifiers = leagueCollection.map(leagueItem => getLeagueIdentifier(leagueItem)!);
      const leaguesToAdd = leagues.filter(leagueItem => {
        const leagueIdentifier = getLeagueIdentifier(leagueItem);
        if (leagueIdentifier == null || leagueCollectionIdentifiers.includes(leagueIdentifier)) {
          return false;
        }
        leagueCollectionIdentifiers.push(leagueIdentifier);
        return true;
      });
      return [...leaguesToAdd, ...leagueCollection];
    }
    return leagueCollection;
  }
}
