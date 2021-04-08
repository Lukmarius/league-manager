jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeague, League } from '../league.model';
import { LeagueService } from '../service/league.service';

import { LeagueRoutingResolveService } from './league-routing-resolve.service';

describe('Service Tests', () => {
  describe('League routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LeagueRoutingResolveService;
    let service: LeagueService;
    let resultLeague: ILeague | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LeagueRoutingResolveService);
      service = TestBed.inject(LeagueService);
      resultLeague = undefined;
    });

    describe('resolve', () => {
      it('should return ILeague returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeague = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeague).toEqual({ id: 123 });
      });

      it('should return new ILeague if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeague = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLeague).toEqual(new League());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeague = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeague).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
