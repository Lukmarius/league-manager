import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MatchResultService } from '../service/match-result.service';

import { MatchResultComponent } from './match-result.component';

describe('Component Tests', () => {
  describe('MatchResult Management Component', () => {
    let comp: MatchResultComponent;
    let fixture: ComponentFixture<MatchResultComponent>;
    let service: MatchResultService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MatchResultComponent],
      })
        .overrideTemplate(MatchResultComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MatchResultComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MatchResultService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.matchResults?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
