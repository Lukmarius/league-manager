jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MatchResultService } from '../service/match-result.service';
import { IMatchResult, MatchResult } from '../match-result.model';

import { MatchResultUpdateComponent } from './match-result-update.component';

describe('Component Tests', () => {
  describe('MatchResult Management Update Component', () => {
    let comp: MatchResultUpdateComponent;
    let fixture: ComponentFixture<MatchResultUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let matchResultService: MatchResultService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MatchResultUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MatchResultUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MatchResultUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      matchResultService = TestBed.inject(MatchResultService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const matchResult: IMatchResult = { id: 456 };

        activatedRoute.data = of({ matchResult });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(matchResult));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const matchResult = { id: 123 };
        spyOn(matchResultService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ matchResult });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: matchResult }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(matchResultService.update).toHaveBeenCalledWith(matchResult);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const matchResult = new MatchResult();
        spyOn(matchResultService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ matchResult });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: matchResult }));
        saveSubject.complete();

        // THEN
        expect(matchResultService.create).toHaveBeenCalledWith(matchResult);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const matchResult = { id: 123 };
        spyOn(matchResultService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ matchResult });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(matchResultService.update).toHaveBeenCalledWith(matchResult);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
