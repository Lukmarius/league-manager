import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeagueStandingDetailComponent } from './league-standing-detail.component';

describe('Component Tests', () => {
  describe('LeagueStanding Management Detail Component', () => {
    let comp: LeagueStandingDetailComponent;
    let fixture: ComponentFixture<LeagueStandingDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LeagueStandingDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ leagueStanding: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LeagueStandingDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeagueStandingDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load leagueStanding on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leagueStanding).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
