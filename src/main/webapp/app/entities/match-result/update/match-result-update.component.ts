import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMatchResult, MatchResult } from '../match-result.model';
import { MatchResultService } from '../service/match-result.service';

@Component({
  selector: 'jhi-match-result-update',
  templateUrl: './match-result-update.component.html',
})
export class MatchResultUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    homeTeamScore: [null, [Validators.required, Validators.min(0)]],
    awayTeamScore: [null, [Validators.required, Validators.min(0)]],
  });

  constructor(protected matchResultService: MatchResultService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matchResult }) => {
      this.updateForm(matchResult);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matchResult = this.createFromForm();
    if (matchResult.id !== undefined) {
      this.subscribeToSaveResponse(this.matchResultService.update(matchResult));
    } else {
      this.subscribeToSaveResponse(this.matchResultService.create(matchResult));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatchResult>>): void {
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

  protected updateForm(matchResult: IMatchResult): void {
    this.editForm.patchValue({
      id: matchResult.id,
      homeTeamScore: matchResult.homeTeamScore,
      awayTeamScore: matchResult.awayTeamScore,
    });
  }

  protected createFromForm(): IMatchResult {
    return {
      ...new MatchResult(),
      id: this.editForm.get(['id'])!.value,
      homeTeamScore: this.editForm.get(['homeTeamScore'])!.value,
      awayTeamScore: this.editForm.get(['awayTeamScore'])!.value,
    };
  }
}
