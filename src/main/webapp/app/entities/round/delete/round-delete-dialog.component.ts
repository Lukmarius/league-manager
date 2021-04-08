import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRound } from '../round.model';
import { RoundService } from '../service/round.service';

@Component({
  templateUrl: './round-delete-dialog.component.html',
})
export class RoundDeleteDialogComponent {
  round?: IRound;

  constructor(protected roundService: RoundService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roundService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
