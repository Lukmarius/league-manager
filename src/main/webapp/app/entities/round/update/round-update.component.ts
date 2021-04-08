import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRound, Round } from '../round.model';
import { RoundService } from '../service/round.service';

@Component({
  selector: 'jhi-round-update',
  templateUrl: './round-update.component.html',
})
export class RoundUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    roundNumber: [],
  });

  constructor(protected roundService: RoundService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ round }) => {
      this.updateForm(round);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const round = this.createFromForm();
    if (round.id !== undefined) {
      this.subscribeToSaveResponse(this.roundService.update(round));
    } else {
      this.subscribeToSaveResponse(this.roundService.create(round));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRound>>): void {
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

  protected updateForm(round: IRound): void {
    this.editForm.patchValue({
      id: round.id,
      roundNumber: round.roundNumber,
    });
  }

  protected createFromForm(): IRound {
    return {
      ...new Round(),
      id: this.editForm.get(['id'])!.value,
      roundNumber: this.editForm.get(['roundNumber'])!.value,
    };
  }
}
