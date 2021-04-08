import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeagueDetailComponent } from './league-detail.component';

describe('Component Tests', () => {
  describe('League Management Detail Component', () => {
    let comp: LeagueDetailComponent;
    let fixture: ComponentFixture<LeagueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LeagueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ league: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LeagueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeagueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load league on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.league).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
