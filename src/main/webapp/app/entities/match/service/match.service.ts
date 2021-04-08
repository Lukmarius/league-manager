import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMatch, getMatchIdentifier } from '../match.model';

export type EntityResponseType = HttpResponse<IMatch>;
export type EntityArrayResponseType = HttpResponse<IMatch[]>;

@Injectable({ providedIn: 'root' })
export class MatchService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/matches');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(match: IMatch): Observable<EntityResponseType> {
    return this.http.post<IMatch>(this.resourceUrl, match, { observe: 'response' });
  }

  update(match: IMatch): Observable<EntityResponseType> {
    return this.http.put<IMatch>(`${this.resourceUrl}/${getMatchIdentifier(match) as number}`, match, { observe: 'response' });
  }

  partialUpdate(match: IMatch): Observable<EntityResponseType> {
    return this.http.patch<IMatch>(`${this.resourceUrl}/${getMatchIdentifier(match) as number}`, match, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMatch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMatch[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMatchToCollectionIfMissing(matchCollection: IMatch[], ...matchesToCheck: (IMatch | null | undefined)[]): IMatch[] {
    const matches: IMatch[] = matchesToCheck.filter(isPresent);
    if (matches.length > 0) {
      const matchCollectionIdentifiers = matchCollection.map(matchItem => getMatchIdentifier(matchItem)!);
      const matchesToAdd = matches.filter(matchItem => {
        const matchIdentifier = getMatchIdentifier(matchItem);
        if (matchIdentifier == null || matchCollectionIdentifiers.includes(matchIdentifier)) {
          return false;
        }
        matchCollectionIdentifiers.push(matchIdentifier);
        return true;
      });
      return [...matchesToAdd, ...matchCollection];
    }
    return matchCollection;
  }
}
