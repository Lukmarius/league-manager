jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeagueStanding, LeagueStanding } from '../league-standing.model';
import { LeagueStandingService } from '../service/league-standing.service';

import { LeagueStandingRoutingResolveService } from './league-standing-routing-resolve.service';

describe('Service Tests', () => {
  describe('LeagueStanding routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LeagueStandingRoutingResolveService;
    let service: LeagueStandingService;
    let resultLeagueStanding: ILeagueStanding | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LeagueStandingRoutingResolveService);
      service = TestBed.inject(LeagueStandingService);
      resultLeagueStanding = undefined;
    });

    describe('resolve', () => {
      it('should return ILeagueStanding returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeagueStanding = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeagueStanding).toEqual({ id: 123 });
      });

      it('should return new ILeagueStanding if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeagueStanding = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLeagueStanding).toEqual(new LeagueStanding());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLeagueStanding = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLeagueStanding).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
