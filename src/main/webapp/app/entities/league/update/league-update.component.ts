import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILeague, League } from '../league.model';
import { LeagueService } from '../service/league.service';

@Component({
  selector: 'jhi-league-update',
  templateUrl: './league-update.component.html',
})
export class LeagueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(protected leagueService: LeagueService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ league }) => {
      this.updateForm(league);
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
      this.subscribeToSaveResponse(this.leagueService.create(league));
    }
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
}
