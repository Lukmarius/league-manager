jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RoundService } from '../service/round.service';
import { IRound, Round } from '../round.model';

import { RoundUpdateComponent } from './round-update.component';

describe('Component Tests', () => {
  describe('Round Management Update Component', () => {
    let comp: RoundUpdateComponent;
    let fixture: ComponentFixture<RoundUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let roundService: RoundService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RoundUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RoundUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RoundUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      roundService = TestBed.inject(RoundService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const round: IRound = { id: 456 };

        activatedRoute.data = of({ round });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(round));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const round = { id: 123 };
        spyOn(roundService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ round });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: round }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(roundService.update).toHaveBeenCalledWith(round);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const round = new Round();
        spyOn(roundService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ round });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: round }));
        saveSubject.complete();

        // THEN
        expect(roundService.create).toHaveBeenCalledWith(round);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const round = { id: 123 };
        spyOn(roundService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ round });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(roundService.update).toHaveBeenCalledWith(round);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
