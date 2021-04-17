import { Component, Input } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeagueStanding } from '../league-standing.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { LeagueStandingService } from '../service/league-standing.service';
import { LeagueStandingDeleteDialogComponent } from '../delete/league-standing-delete-dialog.component';

@Component({
  selector: 'jhi-league-standing',
  templateUrl: './league-standing.component.html',
})
export class LeagueStandingComponent {
  @Input() leagueStandings?: ILeagueStanding[] | null | undefined;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = 30;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected leagueStandingService: LeagueStandingService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.leagueStandingService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ILeagueStanding[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  // ngOnInit(): void {
  //   this.handleNavigation();
  // }

  trackId(index: number, item: ILeagueStanding): number {
    return item.id!;
  }

  delete(leagueStanding: ILeagueStanding): void {
    const modalRef = this.modalService.open(LeagueStandingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.leagueStanding = leagueStanding;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  countMatches(l: ILeagueStanding): number {
    const wins = l.wins ? l.wins : 0;
    const draws = l.draws ? l.draws : 0;
    const losses = l.losses ? l.losses : 0;
    return wins + draws + losses;
  }

  countBalance(l: ILeagueStanding): number {
    const scored = l.scoredGoals ? l.scoredGoals : 0;
    const lost = l.lostGoals ? l.lostGoals : 0;
    return scored - lost;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ILeagueStanding[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/league-standing'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.leagueStandings = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
