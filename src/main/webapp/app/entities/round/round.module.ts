import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RoundComponent } from './list/round.component';
import { RoundDetailComponent } from './detail/round-detail.component';
import { RoundUpdateComponent } from './update/round-update.component';
import { RoundDeleteDialogComponent } from './delete/round-delete-dialog.component';
import { RoundRoutingModule } from './route/round-routing.module';
import { MatchModule } from 'app/entities/match/match.module';

@NgModule({
  imports: [SharedModule, RoundRoutingModule, MatchModule],
  declarations: [RoundComponent, RoundDetailComponent, RoundUpdateComponent, RoundDeleteDialogComponent],
  entryComponents: [RoundDeleteDialogComponent],
  exports: [RoundDetailComponent],
})
export class RoundModule {}
