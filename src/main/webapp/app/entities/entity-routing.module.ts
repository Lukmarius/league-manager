import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'team',
        data: { pageTitle: 'leagueManagerApp.team.home.title' },
        loadChildren: () => import('./team/team.module').then(m => m.TeamModule),
      },
      {
        path: 'match-result',
        data: { pageTitle: 'leagueManagerApp.matchResult.home.title' },
        loadChildren: () => import('./match-result/match-result.module').then(m => m.MatchResultModule),
      },
      {
        path: 'match',
        data: { pageTitle: 'leagueManagerApp.match.home.title' },
        loadChildren: () => import('./match/match.module').then(m => m.MatchModule),
      },
      {
        path: 'round',
        data: { pageTitle: 'leagueManagerApp.round.home.title' },
        loadChildren: () => import('./round/round.module').then(m => m.RoundModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
