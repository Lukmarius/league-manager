import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeagueStanding } from '../league-standing.model';

@Component({
  selector: 'jhi-league-standing-detail',
  templateUrl: './league-standing-detail.component.html',
})
export class LeagueStandingDetailComponent implements OnInit {
  leagueStanding: ILeagueStanding | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leagueStanding }) => {
      this.leagueStanding = leagueStanding;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
