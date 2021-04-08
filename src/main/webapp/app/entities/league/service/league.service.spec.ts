import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILeague, League } from '../league.model';

import { LeagueService } from './league.service';

describe('Service Tests', () => {
  describe('League Service', () => {
    let service: LeagueService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeague;
    let expectedResult: ILeague | ILeague[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LeagueService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a League', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new League()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a League', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a League', () => {
        const patchObject = Object.assign({}, new League());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of League', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a League', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLeagueToCollectionIfMissing', () => {
        it('should add a League to an empty array', () => {
          const league: ILeague = { id: 123 };
          expectedResult = service.addLeagueToCollectionIfMissing([], league);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(league);
        });

        it('should not add a League to an array that contains it', () => {
          const league: ILeague = { id: 123 };
          const leagueCollection: ILeague[] = [
            {
              ...league,
            },
            { id: 456 },
          ];
          expectedResult = service.addLeagueToCollectionIfMissing(leagueCollection, league);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a League to an array that doesn't contain it", () => {
          const league: ILeague = { id: 123 };
          const leagueCollection: ILeague[] = [{ id: 456 }];
          expectedResult = service.addLeagueToCollectionIfMissing(leagueCollection, league);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(league);
        });

        it('should add only unique League to an array', () => {
          const leagueArray: ILeague[] = [{ id: 123 }, { id: 456 }, { id: 13709 }];
          const leagueCollection: ILeague[] = [{ id: 123 }];
          expectedResult = service.addLeagueToCollectionIfMissing(leagueCollection, ...leagueArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const league: ILeague = { id: 123 };
          const league2: ILeague = { id: 456 };
          expectedResult = service.addLeagueToCollectionIfMissing([], league, league2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(league);
          expect(expectedResult).toContain(league2);
        });

        it('should accept null and undefined values', () => {
          const league: ILeague = { id: 123 };
          expectedResult = service.addLeagueToCollectionIfMissing([], null, league, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(league);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
