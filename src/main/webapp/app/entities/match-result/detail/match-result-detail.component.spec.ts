import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MatchResultDetailComponent } from './match-result-detail.component';

describe('Component Tests', () => {
  describe('MatchResult Management Detail Component', () => {
    let comp: MatchResultDetailComponent;
    let fixture: ComponentFixture<MatchResultDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MatchResultDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ matchResult: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MatchResultDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MatchResultDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load matchResult on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.matchResult).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
