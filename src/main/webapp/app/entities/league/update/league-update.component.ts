import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILeague, League } from '../league.model';
import { LeagueService } from '../service/league.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ILeagueRequest, LeagueRequest } from 'app/entities/league/league-request.model';

@Component({
  selector: 'jhi-league-update',
  templateUrl: './league-update.component.html',
})
export class LeagueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    teams: [],
  });
  teamsSharedCollection: ITeam[] = [];

  constructor(
    protected leagueService: LeagueService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ league }) => {
      this.updateForm(league);
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const league = this.createFromForm();
    if (league.id !== undefined) {
      this.subscribeToSaveResponse(this.leagueService.update(league));
    } else {
      const request = this.createLeagueRequestFromForm();
      this.subscribeToSaveResponse(this.leagueService.create(request));
    }
  }

  getSelectedTeam(option: ITeam, selectedVals: ITeam[]): ITeam {
    for (const selectedVal of selectedVals) {
      if (option.id === selectedVal.id) {
        return selectedVal;
      }
    }
    return option;
  }

  trackTeamById(index: number, item: ITeam): number | undefined {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeague>>): void {
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

  protected updateForm(league: ILeague): void {
    this.editForm.patchValue({
      id: league.id,
      name: league.name,
    });
  }

  protected createFromForm(): ILeague {
    return {
      ...new League(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }

  protected createLeagueRequestFromForm(): ILeagueRequest {
    return {
      ...new League(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      teams: this.editForm.get(['teams'])!.value,
    };
  }

  protected loadRelationshipsOptions(): void {
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, ...(this.editForm.get('teams')!.value ?? []))))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }
}
