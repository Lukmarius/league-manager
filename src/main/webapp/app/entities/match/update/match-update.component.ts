import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMatch, Match } from '../match.model';
import { MatchService } from '../service/match.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IMatchResult } from 'app/entities/match-result/match-result.model';
import { MatchResultService } from 'app/entities/match-result/service/match-result.service';

@Component({
  selector: 'jhi-match-update',
  templateUrl: './match-update.component.html',
})
export class MatchUpdateComponent implements OnInit {
  isSaving = false;

  teamsSharedCollection: ITeam[] = [];
  matchResultsCollection: IMatchResult[] = [];

  editForm = this.fb.group({
    id: [],
    homeTeam: [],
    awayTeam: [],
    matchResult: [],
  });

  constructor(
    protected matchService: MatchService,
    protected teamService: TeamService,
    protected matchResultService: MatchResultService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ match }) => {
      this.updateForm(match);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const match = this.createFromForm();
    if (match.id !== undefined) {
      this.subscribeToSaveResponse(this.matchService.update(match));
    } else {
      this.subscribeToSaveResponse(this.matchService.create(match));
    }
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  trackMatchResultById(index: number, item: IMatchResult): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatch>>): void {
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

  protected updateForm(match: IMatch): void {
    this.editForm.patchValue({
      id: match.id,
      homeTeam: match.homeTeam,
      awayTeam: match.awayTeam,
      matchResult: match.matchResult,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, match.homeTeam, match.awayTeam);
    this.matchResultsCollection = this.matchResultService.addMatchResultToCollectionIfMissing(
      this.matchResultsCollection,
      match.matchResult
    );
  }

  protected loadRelationshipsOptions(): void {
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(
        map((teams: ITeam[]) =>
          this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('homeTeam')!.value, this.editForm.get('awayTeam')!.value)
        )
      )
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.matchResultService
      .query({ filter: 'match-is-null' })
      .pipe(map((res: HttpResponse<IMatchResult[]>) => res.body ?? []))
      .pipe(
        map((matchResults: IMatchResult[]) =>
          this.matchResultService.addMatchResultToCollectionIfMissing(matchResults, this.editForm.get('matchResult')!.value)
        )
      )
      .subscribe((matchResults: IMatchResult[]) => (this.matchResultsCollection = matchResults));
  }

  protected createFromForm(): IMatch {
    return {
      ...new Match(),
      id: this.editForm.get(['id'])!.value,
      homeTeam: this.editForm.get(['homeTeam'])!.value,
      awayTeam: this.editForm.get(['awayTeam'])!.value,
      matchResult: this.editForm.get(['matchResult'])!.value,
    };
  }
}
