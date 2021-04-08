import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeagueStanding } from '../league-standing.model';
import { LeagueStandingService } from '../service/league-standing.service';

@Component({
  templateUrl: './league-standing-delete-dialog.component.html',
})
export class LeagueStandingDeleteDialogComponent {
  leagueStanding?: ILeagueStanding;

  constructor(protected leagueStandingService: LeagueStandingService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leagueStandingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
