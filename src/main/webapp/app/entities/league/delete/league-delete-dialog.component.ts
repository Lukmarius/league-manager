import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeague } from '../league.model';
import { LeagueService } from '../service/league.service';

@Component({
  templateUrl: './league-delete-dialog.component.html',
})
export class LeagueDeleteDialogComponent {
  league?: ILeague;

  constructor(protected leagueService: LeagueService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leagueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
