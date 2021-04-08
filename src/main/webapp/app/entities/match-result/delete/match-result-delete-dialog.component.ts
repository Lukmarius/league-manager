import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatchResult } from '../match-result.model';
import { MatchResultService } from '../service/match-result.service';

@Component({
  templateUrl: './match-result-delete-dialog.component.html',
})
export class MatchResultDeleteDialogComponent {
  matchResult?: IMatchResult;

  constructor(protected matchResultService: MatchResultService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.matchResultService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
