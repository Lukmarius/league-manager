jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMatch, Match } from '../match.model';
import { MatchService } from '../service/match.service';

import { MatchRoutingResolveService } from './match-routing-resolve.service';

describe('Service Tests', () => {
  describe('Match routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MatchRoutingResolveService;
    let service: MatchService;
    let resultMatch: IMatch | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MatchRoutingResolveService);
      service = TestBed.inject(MatchService);
      resultMatch = undefined;
    });

    describe('resolve', () => {
      it('should return IMatch returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMatch = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMatch).toEqual({ id: 123 });
      });

      it('should return new IMatch if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMatch = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMatch).toEqual(new Match());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMatch = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMatch).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
