jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MatchService } from '../service/match.service';
import { IMatch, Match } from '../match.model';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IMatchResult } from 'app/entities/match-result/match-result.model';
import { MatchResultService } from 'app/entities/match-result/service/match-result.service';
import { IRound } from 'app/entities/round/round.model';
import { RoundService } from 'app/entities/round/service/round.service';

import { MatchUpdateComponent } from './match-update.component';

describe('Component Tests', () => {
  describe('Match Management Update Component', () => {
    let comp: MatchUpdateComponent;
    let fixture: ComponentFixture<MatchUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let matchService: MatchService;
    let teamService: TeamService;
    let matchResultService: MatchResultService;
    let roundService: RoundService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MatchUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MatchUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MatchUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      matchService = TestBed.inject(MatchService);
      teamService = TestBed.inject(TeamService);
      matchResultService = TestBed.inject(MatchResultService);
      roundService = TestBed.inject(RoundService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Team query and add missing value', () => {
        const match: IMatch = { id: 456 };
        const homeTeam: ITeam = { id: 65044 };
        match.homeTeam = homeTeam;
        const awayTeam: ITeam = { id: 41436 };
        match.awayTeam = awayTeam;

        const teamCollection: ITeam[] = [{ id: 12627 }];
        spyOn(teamService, 'query').and.returnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [homeTeam, awayTeam];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        spyOn(teamService, 'addTeamToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ match });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call matchResult query and add missing value', () => {
        const match: IMatch = { id: 456 };
        const matchResult: IMatchResult = { id: 25564 };
        match.matchResult = matchResult;

        const matchResultCollection: IMatchResult[] = [{ id: 62671 }];
        spyOn(matchResultService, 'query').and.returnValue(of(new HttpResponse({ body: matchResultCollection })));
        const expectedCollection: IMatchResult[] = [matchResult, ...matchResultCollection];
        spyOn(matchResultService, 'addMatchResultToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ match });
        comp.ngOnInit();

        expect(matchResultService.query).toHaveBeenCalled();
        expect(matchResultService.addMatchResultToCollectionIfMissing).toHaveBeenCalledWith(matchResultCollection, matchResult);
        expect(comp.matchResultsCollection).toEqual(expectedCollection);
      });

      it('Should call Round query and add missing value', () => {
        const match: IMatch = { id: 456 };
        const round: IRound = { id: 40014 };
        match.round = round;

        const roundCollection: IRound[] = [{ id: 79214 }];
        spyOn(roundService, 'query').and.returnValue(of(new HttpResponse({ body: roundCollection })));
        const additionalRounds = [round];
        const expectedCollection: IRound[] = [...additionalRounds, ...roundCollection];
        spyOn(roundService, 'addRoundToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ match });
        comp.ngOnInit();

        expect(roundService.query).toHaveBeenCalled();
        expect(roundService.addRoundToCollectionIfMissing).toHaveBeenCalledWith(roundCollection, ...additionalRounds);
        expect(comp.roundsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const match: IMatch = { id: 456 };
        const homeTeam: ITeam = { id: 97157 };
        match.homeTeam = homeTeam;
        const awayTeam: ITeam = { id: 51073 };
        match.awayTeam = awayTeam;
        const matchResult: IMatchResult = { id: 76137 };
        match.matchResult = matchResult;
        const round: IRound = { id: 58656 };
        match.round = round;

        activatedRoute.data = of({ match });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(match));
        expect(comp.teamsSharedCollection).toContain(homeTeam);
        expect(comp.teamsSharedCollection).toContain(awayTeam);
        expect(comp.matchResultsCollection).toContain(matchResult);
        expect(comp.roundsSharedCollection).toContain(round);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const match = { id: 123 };
        spyOn(matchService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ match });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: match }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(matchService.update).toHaveBeenCalledWith(match);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const match = new Match();
        spyOn(matchService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ match });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: match }));
        saveSubject.complete();

        // THEN
        expect(matchService.create).toHaveBeenCalledWith(match);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const match = { id: 123 };
        spyOn(matchService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ match });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(matchService.update).toHaveBeenCalledWith(match);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTeamById', () => {
        it('Should return tracked Team primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTeamById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMatchResultById', () => {
        it('Should return tracked MatchResult primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMatchResultById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackRoundById', () => {
        it('Should return tracked Round primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRoundById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
