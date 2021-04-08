import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILeagueStanding, LeagueStanding } from '../league-standing.model';
import { LeagueStandingService } from '../service/league-standing.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ILeague } from 'app/entities/league/league.model';
import { LeagueService } from 'app/entities/league/service/league.service';

@Component({
  selector: 'jhi-league-standing-update',
  templateUrl: './league-standing-update.component.html',
})
export class LeagueStandingUpdateComponent implements OnInit {
  isSaving = false;

  teamsSharedCollection: ITeam[] = [];
  leaguesSharedCollection: ILeague[] = [];

  editForm = this.fb.group({
    id: [],
    position: [null, [Validators.required, Validators.min(1)]],
    points: [null, [Validators.required]],
    scoredGoals: [null, [Validators.required, Validators.min(0)]],
    lostGoals: [null, [Validators.required, Validators.min(0)]],
    wins: [null, [Validators.required, Validators.min(0)]],
    draws: [null, [Validators.required, Validators.min(0)]],
    losses: [null, [Validators.required, Validators.min(0)]],
    team: [null, Validators.required],
    league: [],
  });

  constructor(
    protected leagueStandingService: LeagueStandingService,
    protected teamService: TeamService,
    protected leagueService: LeagueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leagueStanding }) => {
      this.updateForm(leagueStanding);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leagueStanding = this.createFromForm();
    if (leagueStanding.id !== undefined) {
      this.subscribeToSaveResponse(this.leagueStandingService.update(leagueStanding));
    } else {
      this.subscribeToSaveResponse(this.leagueStandingService.create(leagueStanding));
    }
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  trackLeagueById(index: number, item: ILeague): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeagueStanding>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(leagueStanding: ILeagueStanding): void {
    this.editForm.patchValue({
      id: leagueStanding.id,
      position: leagueStanding.position,
      points: leagueStanding.points,
      scoredGoals: leagueStanding.scoredGoals,
      lostGoals: leagueStanding.lostGoals,
      wins: leagueStanding.wins,
      draws: leagueStanding.draws,
      losses: leagueStanding.losses,
      team: leagueStanding.team,
      league: leagueStanding.league,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, leagueStanding.team);
    this.leaguesSharedCollection = this.leagueService.addLeagueToCollectionIfMissing(this.leaguesSharedCollection, leagueStanding.league);
  }

  protected loadRelationshipsOptions(): void {
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.leagueService
      .query()
      .pipe(map((res: HttpResponse<ILeague[]>) => res.body ?? []))
      .pipe(map((leagues: ILeague[]) => this.leagueService.addLeagueToCollectionIfMissing(leagues, this.editForm.get('league')!.value)))
      .subscribe((leagues: ILeague[]) => (this.leaguesSharedCollection = leagues));
  }

  protected createFromForm(): ILeagueStanding {
    return {
      ...new LeagueStanding(),
      id: this.editForm.get(['id'])!.value,
      position: this.editForm.get(['position'])!.value,
      points: this.editForm.get(['points'])!.value,
      scoredGoals: this.editForm.get(['scoredGoals'])!.value,
      lostGoals: this.editForm.get(['lostGoals'])!.value,
      wins: this.editForm.get(['wins'])!.value,
      draws: this.editForm.get(['draws'])!.value,
      losses: this.editForm.get(['losses'])!.value,
      team: this.editForm.get(['team'])!.value,
      league: this.editForm.get(['league'])!.value,
    };
  }
}
