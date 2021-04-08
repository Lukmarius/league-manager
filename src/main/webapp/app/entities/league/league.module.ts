import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LeagueComponent } from './list/league.component';
import { LeagueDetailComponent } from './detail/league-detail.component';
import { LeagueUpdateComponent } from './update/league-update.component';
import { LeagueDeleteDialogComponent } from './delete/league-delete-dialog.component';
import { LeagueRoutingModule } from './route/league-routing.module';

@NgModule({
  imports: [SharedModule, LeagueRoutingModule],
  declarations: [LeagueComponent, LeagueDetailComponent, LeagueUpdateComponent, LeagueDeleteDialogComponent],
  entryComponents: [LeagueDeleteDialogComponent],
})
export class LeagueModule {}
