import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILeagueStanding, LeagueStanding } from '../league-standing.model';

import { LeagueStandingService } from './league-standing.service';

describe('Service Tests', () => {
  describe('LeagueStanding Service', () => {
    let service: LeagueStandingService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeagueStanding;
    let expectedResult: ILeagueStanding | ILeagueStanding[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LeagueStandingService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        position: 0,
        points: 0,
        scoredGoals: 0,
        lostGoals: 0,
        wins: 0,
        draws: 0,
        losses: 0,
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

      it('should create a LeagueStanding', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LeagueStanding()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LeagueStanding', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            position: 1,
            points: 1,
            scoredGoals: 1,
            lostGoals: 1,
            wins: 1,
            draws: 1,
            losses: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a LeagueStanding', () => {
        const patchObject = Object.assign(
          {
            points: 1,
            scoredGoals: 1,
            lostGoals: 1,
            wins: 1,
            draws: 1,
            losses: 1,
          },
          new LeagueStanding()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LeagueStanding', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            position: 1,
            points: 1,
            scoredGoals: 1,
            lostGoals: 1,
            wins: 1,
            draws: 1,
            losses: 1,
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

      it('should delete a LeagueStanding', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLeagueStandingToCollectionIfMissing', () => {
        it('should add a LeagueStanding to an empty array', () => {
          const leagueStanding: ILeagueStanding = { id: 123 };
          expectedResult = service.addLeagueStandingToCollectionIfMissing([], leagueStanding);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leagueStanding);
        });

        it('should not add a LeagueStanding to an array that contains it', () => {
          const leagueStanding: ILeagueStanding = { id: 123 };
          const leagueStandingCollection: ILeagueStanding[] = [
            {
              ...leagueStanding,
            },
            { id: 456 },
          ];
          expectedResult = service.addLeagueStandingToCollectionIfMissing(leagueStandingCollection, leagueStanding);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LeagueStanding to an array that doesn't contain it", () => {
          const leagueStanding: ILeagueStanding = { id: 123 };
          const leagueStandingCollection: ILeagueStanding[] = [{ id: 456 }];
          expectedResult = service.addLeagueStandingToCollectionIfMissing(leagueStandingCollection, leagueStanding);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leagueStanding);
        });

        it('should add only unique LeagueStanding to an array', () => {
          const leagueStandingArray: ILeagueStanding[] = [{ id: 123 }, { id: 456 }, { id: 70285 }];
          const leagueStandingCollection: ILeagueStanding[] = [{ id: 123 }];
          expectedResult = service.addLeagueStandingToCollectionIfMissing(leagueStandingCollection, ...leagueStandingArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const leagueStanding: ILeagueStanding = { id: 123 };
          const leagueStanding2: ILeagueStanding = { id: 456 };
          expectedResult = service.addLeagueStandingToCollectionIfMissing([], leagueStanding, leagueStanding2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leagueStanding);
          expect(expectedResult).toContain(leagueStanding2);
        });

        it('should accept null and undefined values', () => {
          const leagueStanding: ILeagueStanding = { id: 123 };
          expectedResult = service.addLeagueStandingToCollectionIfMissing([], null, leagueStanding, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leagueStanding);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
