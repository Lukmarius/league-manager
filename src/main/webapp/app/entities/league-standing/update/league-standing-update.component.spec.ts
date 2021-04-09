jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeagueStandingService } from '../service/league-standing.service';
import { ILeagueStanding, LeagueStanding } from '../league-standing.model';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ILeague } from 'app/entities/league/league.model';
import { LeagueService } from 'app/entities/league/service/league.service';

import { LeagueStandingUpdateComponent } from './league-standing-update.component';

describe('Component Tests', () => {
  describe('LeagueStanding Management Update Component', () => {
    let comp: LeagueStandingUpdateComponent;
    let fixture: ComponentFixture<LeagueStandingUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let leagueStandingService: LeagueStandingService;
    let teamService: TeamService;
    let leagueService: LeagueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LeagueStandingUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LeagueStandingUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeagueStandingUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      leagueStandingService = TestBed.inject(LeagueStandingService);
      teamService = TestBed.inject(TeamService);
      leagueService = TestBed.inject(LeagueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Team query and add missing value', () => {
        const leagueStanding: ILeagueStanding = { id: 456 };
        const team: ITeam = { id: 53574 };
        leagueStanding.team = team;

        const teamCollection: ITeam[] = [{ id: 58730 }];
        spyOn(teamService, 'query').and.returnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [team];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        spyOn(teamService, 'addTeamToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ leagueStanding });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call League query and add missing value', () => {
        const leagueStanding: ILeagueStanding = { id: 456 };
        const league: ILeague = { id: 81930 };
        leagueStanding.league = league;

        const leagueCollection: ILeague[] = [{ id: 64552 }];
        spyOn(leagueService, 'query').and.returnValue(of(new HttpResponse({ body: leagueCollection })));
        const additionalLeagues = [league];
        const expectedCollection: ILeague[] = [...additionalLeagues, ...leagueCollection];
        spyOn(leagueService, 'addLeagueToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ leagueStanding });
        comp.ngOnInit();

        expect(leagueService.query).toHaveBeenCalled();
        expect(leagueService.addLeagueToCollectionIfMissing).toHaveBeenCalledWith(leagueCollection, ...additionalLeagues);
        expect(comp.leaguesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const leagueStanding: ILeagueStanding = { id: 456 };
        const team: ITeam = { id: 54534 };
        leagueStanding.team = team;
        const league: ILeague = { id: 66305 };
        leagueStanding.league = league;

        activatedRoute.data = of({ leagueStanding });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(leagueStanding));
        expect(comp.teamsSharedCollection).toContain(team);
        expect(comp.leaguesSharedCollection).toContain(league);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leagueStanding = { id: 123 };
        spyOn(leagueStandingService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leagueStanding });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leagueStanding }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(leagueStandingService.update).toHaveBeenCalledWith(leagueStanding);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leagueStanding = new LeagueStanding();
        spyOn(leagueStandingService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leagueStanding });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leagueStanding }));
        saveSubject.complete();

        // THEN
        expect(leagueStandingService.create).toHaveBeenCalledWith(leagueStanding);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leagueStanding = { id: 123 };
        spyOn(leagueStandingService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leagueStanding });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(leagueStandingService.update).toHaveBeenCalledWith(leagueStanding);
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

      describe('trackLeagueById', () => {
        it('Should return tracked League primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLeagueById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
