import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeague } from '../league.model';
import { ILeagueStanding } from 'app/entities/league-standing/league-standing.model';
import { LeagueRoutingResolveService } from 'app/entities/league/route/league-routing-resolve.service';
import { LeagueService } from 'app/entities/league/service/league.service';

@Component({
  selector: 'jhi-league-detail',
  templateUrl: './league-detail.component.html',
})
export class LeagueDetailComponent implements OnInit {
  league: ILeague | undefined | null = null;
  standings: ILeagueStanding[] | undefined | null = null;

  constructor(protected activatedRoute: ActivatedRoute, private leagueService: LeagueService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ league }) => {
      this.league = league;
      this.standings = this.league?.leagueStandings;
    });
  }

  previousState(): void {
    window.history.back();
  }

  onNewResult(): void {
    this.leagueService.resolve(this.league?.id).subscribe(league => {
      this.league = league;
      this.standings = this.league.leagueStandings;
    });
  }
}
