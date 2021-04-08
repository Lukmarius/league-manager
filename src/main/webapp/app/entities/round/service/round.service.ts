import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRound, getRoundIdentifier } from '../round.model';

export type EntityResponseType = HttpResponse<IRound>;
export type EntityArrayResponseType = HttpResponse<IRound[]>;

@Injectable({ providedIn: 'root' })
export class RoundService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/rounds');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(round: IRound): Observable<EntityResponseType> {
    return this.http.post<IRound>(this.resourceUrl, round, { observe: 'response' });
  }

  update(round: IRound): Observable<EntityResponseType> {
    return this.http.put<IRound>(`${this.resourceUrl}/${getRoundIdentifier(round) as number}`, round, { observe: 'response' });
  }

  partialUpdate(round: IRound): Observable<EntityResponseType> {
    return this.http.patch<IRound>(`${this.resourceUrl}/${getRoundIdentifier(round) as number}`, round, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRound>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRound[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRoundToCollectionIfMissing(roundCollection: IRound[], ...roundsToCheck: (IRound | null | undefined)[]): IRound[] {
    const rounds: IRound[] = roundsToCheck.filter(isPresent);
    if (rounds.length > 0) {
      const roundCollectionIdentifiers = roundCollection.map(roundItem => getRoundIdentifier(roundItem)!);
      const roundsToAdd = rounds.filter(roundItem => {
        const roundIdentifier = getRoundIdentifier(roundItem);
        if (roundIdentifier == null || roundCollectionIdentifiers.includes(roundIdentifier)) {
          return false;
        }
        roundCollectionIdentifiers.push(roundIdentifier);
        return true;
      });
      return [...roundsToAdd, ...roundCollection];
    }
    return roundCollection;
  }
}
