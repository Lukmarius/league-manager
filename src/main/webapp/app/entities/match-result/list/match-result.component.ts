import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatchResult } from '../match-result.model';
import { MatchResultService } from '../service/match-result.service';
import { MatchResultDeleteDialogComponent } from '../delete/match-result-delete-dialog.component';

@Component({
  selector: 'jhi-match-result',
  templateUrl: './match-result.component.html',
})
export class MatchResultComponent implements OnInit {
  matchResults?: IMatchResult[];
  isLoading = false;

  constructor(protected matchResultService: MatchResultService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.matchResultService.query().subscribe(
      (res: HttpResponse<IMatchResult[]>) => {
        this.isLoading = false;
        this.matchResults = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMatchResult): number {
    return item.id!;
  }

  delete(matchResult: IMatchResult): void {
    const modalRef = this.modalService.open(MatchResultDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.matchResult = matchResult;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
