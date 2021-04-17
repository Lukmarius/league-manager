import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LeagueComponent } from './list/league.component';
import { LeagueDetailComponent } from './detail/league-detail.component';
import { LeagueUpdateComponent } from './update/league-update.component';
import { LeagueDeleteDialogComponent } from './delete/league-delete-dialog.component';
import { LeagueRoutingModule } from './route/league-routing.module';
import { LeagueStandingModule } from 'app/entities/league-standing/league-standing.module';
import { RoundModule } from 'app/entities/round/round.module';

@NgModule({
  imports: [SharedModule, LeagueRoutingModule, LeagueStandingModule, RoundModule],
  declarations: [LeagueComponent, LeagueDetailComponent, LeagueUpdateComponent, LeagueDeleteDialogComponent],
  entryComponents: [LeagueDeleteDialogComponent],
})
export class LeagueModule {}
