import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatchResult } from '../match-result.model';

@Component({
  selector: 'jhi-match-result-detail',
  templateUrl: './match-result-detail.component.html',
})
export class MatchResultDetailComponent implements OnInit {
  matchResult: IMatchResult | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matchResult }) => {
      this.matchResult = matchResult;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
