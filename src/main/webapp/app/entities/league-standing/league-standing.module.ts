import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LeagueStandingComponent } from './list/league-standing.component';
import { LeagueStandingDetailComponent } from './detail/league-standing-detail.component';
import { LeagueStandingUpdateComponent } from './update/league-standing-update.component';
import { LeagueStandingDeleteDialogComponent } from './delete/league-standing-delete-dialog.component';
import { LeagueStandingRoutingModule } from './route/league-standing-routing.module';

@NgModule({
  imports: [SharedModule, LeagueStandingRoutingModule],
  declarations: [
    LeagueStandingComponent,
    LeagueStandingDetailComponent,
    LeagueStandingUpdateComponent,
    LeagueStandingDeleteDialogComponent,
  ],
  entryComponents: [LeagueStandingDeleteDialogComponent],
})
export class LeagueStandingModule {}
