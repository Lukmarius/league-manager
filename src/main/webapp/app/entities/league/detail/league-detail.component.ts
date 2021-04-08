import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeague } from '../league.model';

@Component({
  selector: 'jhi-league-detail',
  templateUrl: './league-detail.component.html',
})
export class LeagueDetailComponent implements OnInit {
  league: ILeague | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ league }) => {
      this.league = league;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
