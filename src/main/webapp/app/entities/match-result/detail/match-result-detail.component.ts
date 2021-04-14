import { Component, ElementRef, HostListener, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatchResult, MatchResult } from '../match-result.model';

@Component({
  selector: 'jhi-match-result-detail',
  templateUrl: './match-result-detail.component.html',
})
export class MatchResultDetailComponent implements OnInit {
  @Input() matchResult: IMatchResult | undefined | null = null;
  editMode = false;

  constructor(protected activatedRoute: ActivatedRoute, private eRef: ElementRef) {}

  ngOnInit(): void {
    if (!this.matchResult) {
      this.activatedRoute.data.subscribe(({ matchResult }) => {
        this.matchResult = matchResult;
      });
    }
  }

  // @HostListener('document:click', ['$event.target'])
  // clickOut(target: EventTarget): void {
  //   if(!this.eRef.nativeElement.contains(target) && this.editMode) {
  //     // save result
  //     this.editMode = false;
  //   }
  // }

  previousState(): void {
    window.history.back();
  }

  switchEditMode(): void {
    this.editMode = !this.editMode;
    // if (this.editMode && !this.matchResult) {
    //   this.matchResult = new MatchResult(undefined, 0, 0);
    // }
  }
}
