import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMatchResult, MatchResult } from '../match-result.model';

import { MatchResultService } from './match-result.service';

describe('Service Tests', () => {
  describe('MatchResult Service', () => {
    let service: MatchResultService;
    let httpMock: HttpTestingController;
    let elemDefault: IMatchResult;
    let expectedResult: IMatchResult | IMatchResult[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MatchResultService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        homeTeamScore: 0,
        awayTeamScore: 0,
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

      it('should create a MatchResult', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MatchResult()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MatchResult', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            homeTeamScore: 1,
            awayTeamScore: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MatchResult', () => {
        const patchObject = Object.assign(
          {
            homeTeamScore: 1,
          },
          new MatchResult()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MatchResult', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            homeTeamScore: 1,
            awayTeamScore: 1,
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

      it('should delete a MatchResult', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMatchResultToCollectionIfMissing', () => {
        it('should add a MatchResult to an empty array', () => {
          const matchResult: IMatchResult = { id: 123 };
          expectedResult = service.addMatchResultToCollectionIfMissing([], matchResult);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(matchResult);
        });

        it('should not add a MatchResult to an array that contains it', () => {
          const matchResult: IMatchResult = { id: 123 };
          const matchResultCollection: IMatchResult[] = [
            {
              ...matchResult,
            },
            { id: 456 },
          ];
          expectedResult = service.addMatchResultToCollectionIfMissing(matchResultCollection, matchResult);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MatchResult to an array that doesn't contain it", () => {
          const matchResult: IMatchResult = { id: 123 };
          const matchResultCollection: IMatchResult[] = [{ id: 456 }];
          expectedResult = service.addMatchResultToCollectionIfMissing(matchResultCollection, matchResult);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(matchResult);
        });

        it('should add only unique MatchResult to an array', () => {
          const matchResultArray: IMatchResult[] = [{ id: 123 }, { id: 456 }, { id: 56135 }];
          const matchResultCollection: IMatchResult[] = [{ id: 123 }];
          expectedResult = service.addMatchResultToCollectionIfMissing(matchResultCollection, ...matchResultArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const matchResult: IMatchResult = { id: 123 };
          const matchResult2: IMatchResult = { id: 456 };
          expectedResult = service.addMatchResultToCollectionIfMissing([], matchResult, matchResult2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(matchResult);
          expect(expectedResult).toContain(matchResult2);
        });

        it('should accept null and undefined values', () => {
          const matchResult: IMatchResult = { id: 123 };
          expectedResult = service.addMatchResultToCollectionIfMissing([], null, matchResult, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(matchResult);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
