import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatch } from '../match.model';
import { IMatchResult } from 'app/entities/match-result/match-result.model';
import { MatchService } from 'app/entities/match/service/match.service';

@Component({
  selector: 'jhi-match-detail',
  templateUrl: './match-detail.component.html',
})
export class MatchDetailComponent implements OnInit {
  @Input() match: IMatch | null | undefined = null;
  editMode = false;

  constructor(protected activatedRoute: ActivatedRoute, protected matchService: MatchService) {}

  ngOnInit(): void {
    if (!this.match) {
      this.activatedRoute.data.subscribe(({ match }) => {
        this.match = match;
      });
    }
  }

  previousState(): void {
    window.history.back();
  }

  onSaveResult($event: IMatchResult): void {
    if (this.match) {
      this.match.matchResult = $event;
      this.matchService.update(this.match).subscribe(() => {
        this.editMode = false;
      });
    }
  }

  onCancelEditResult(): void {
    this.editMode = false;
  }

  turnOnEditMode(): void {
    this.editMode = true;
  }
}
