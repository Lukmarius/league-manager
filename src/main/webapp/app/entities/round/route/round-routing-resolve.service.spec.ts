jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRound, Round } from '../round.model';
import { RoundService } from '../service/round.service';

import { RoundRoutingResolveService } from './round-routing-resolve.service';

describe('Service Tests', () => {
  describe('Round routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RoundRoutingResolveService;
    let service: RoundService;
    let resultRound: IRound | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RoundRoutingResolveService);
      service = TestBed.inject(RoundService);
      resultRound = undefined;
    });

    describe('resolve', () => {
      it('should return IRound returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRound = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRound).toEqual({ id: 123 });
      });

      it('should return new IRound if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRound = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRound).toEqual(new Round());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRound = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRound).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
