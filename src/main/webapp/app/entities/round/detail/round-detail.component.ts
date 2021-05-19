import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRound } from '../round.model';

@Component({
  selector: 'jhi-round-detail',
  templateUrl: './round-detail.component.html',
})
export class RoundDetailComponent implements OnInit {
  @Input() round: IRound | undefined | null = null;
  @Output() newResult: EventEmitter<void> = new EventEmitter<void>();

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    if (!this.round) {
      this.activatedRoute.data.subscribe(({ round }) => {
        this.round = round;
      });
    }
  }

  previousState(): void {
    window.history.back();
  }

  onNewResult(): void {
    this.newResult.emit();
  }
}
