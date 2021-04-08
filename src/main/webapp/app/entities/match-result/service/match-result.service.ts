import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMatchResult, getMatchResultIdentifier } from '../match-result.model';

export type EntityResponseType = HttpResponse<IMatchResult>;
export type EntityArrayResponseType = HttpResponse<IMatchResult[]>;

@Injectable({ providedIn: 'root' })
export class MatchResultService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/match-results');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(matchResult: IMatchResult): Observable<EntityResponseType> {
    return this.http.post<IMatchResult>(this.resourceUrl, matchResult, { observe: 'response' });
  }

  update(matchResult: IMatchResult): Observable<EntityResponseType> {
    return this.http.put<IMatchResult>(`${this.resourceUrl}/${getMatchResultIdentifier(matchResult) as number}`, matchResult, {
      observe: 'response',
    });
  }

  partialUpdate(matchResult: IMatchResult): Observable<EntityResponseType> {
    return this.http.patch<IMatchResult>(`${this.resourceUrl}/${getMatchResultIdentifier(matchResult) as number}`, matchResult, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMatchResult>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMatchResult[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMatchResultToCollectionIfMissing(
    matchResultCollection: IMatchResult[],
    ...matchResultsToCheck: (IMatchResult | null | undefined)[]
  ): IMatchResult[] {
    const matchResults: IMatchResult[] = matchResultsToCheck.filter(isPresent);
    if (matchResults.length > 0) {
      const matchResultCollectionIdentifiers = matchResultCollection.map(matchResultItem => getMatchResultIdentifier(matchResultItem)!);
      const matchResultsToAdd = matchResults.filter(matchResultItem => {
        const matchResultIdentifier = getMatchResultIdentifier(matchResultItem);
        if (matchResultIdentifier == null || matchResultCollectionIdentifiers.includes(matchResultIdentifier)) {
          return false;
        }
        matchResultCollectionIdentifiers.push(matchResultIdentifier);
        return true;
      });
      return [...matchResultsToAdd, ...matchResultCollection];
    }
    return matchResultCollection;
  }
}
