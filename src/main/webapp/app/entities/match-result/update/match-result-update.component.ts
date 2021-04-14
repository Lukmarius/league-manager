import { Component, Input, OnInit, Output } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMatchResult, MatchResult } from '../match-result.model';
import { MatchResultService } from '../service/match-result.service';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'jhi-match-result-update',
  templateUrl: './match-result-update.component.html',
})
export class MatchResultUpdateComponent implements OnInit {
  @Input() matchResult: IMatchResult | undefined | null;
  @Output() cancellation = new EventEmitter<void>();
  @Output() saving = new EventEmitter<IMatchResult>();

  isSaving = false;

  editForm = this.fb.group({
    id: [],
    homeTeamScore: [null, [Validators.required, Validators.min(0)]],
    awayTeamScore: [null, [Validators.required, Validators.min(0)]],
  });

  constructor(protected matchResultService: MatchResultService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    if (!this.matchResult) {
      this.matchResult = new MatchResult(undefined, 0, 0);
    }
    this.updateForm();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.matchResult = this.createFromForm();
    this.saving.emit(this.matchResult);
  }

  cancel(): void {
    this.cancellation.emit();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatchResult>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    // Api for inheritance.
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(): void {
    this.editForm.patchValue({
      id: this.matchResult?.id,
      homeTeamScore: this.matchResult?.homeTeamScore,
      awayTeamScore: this.matchResult?.awayTeamScore,
    });
  }

  protected createFromForm(): IMatchResult {
    return {
      ...this.matchResult,
      id: this.matchResult?.id,
      homeTeamScore: this.editForm.get(['homeTeamScore'])!.value,
      awayTeamScore: this.editForm.get(['awayTeamScore'])!.value,
    };
  }
}
