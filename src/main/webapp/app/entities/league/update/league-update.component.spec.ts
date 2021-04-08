jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeagueService } from '../service/league.service';
import { ILeague, League } from '../league.model';

import { LeagueUpdateComponent } from './league-update.component';

describe('Component Tests', () => {
  describe('League Management Update Component', () => {
    let comp: LeagueUpdateComponent;
    let fixture: ComponentFixture<LeagueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let leagueService: LeagueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LeagueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LeagueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeagueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      leagueService = TestBed.inject(LeagueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const league: ILeague = { id: 456 };

        activatedRoute.data = of({ league });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(league));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const league = { id: 123 };
        spyOn(leagueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ league });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: league }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(leagueService.update).toHaveBeenCalledWith(league);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const league = new League();
        spyOn(leagueService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ league });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: league }));
        saveSubject.complete();

        // THEN
        expect(leagueService.create).toHaveBeenCalledWith(league);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const league = { id: 123 };
        spyOn(leagueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ league });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(leagueService.update).toHaveBeenCalledWith(league);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
