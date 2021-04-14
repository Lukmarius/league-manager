import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MatchComponent } from './list/match.component';
import { MatchDetailComponent } from './detail/match-detail.component';
import { MatchUpdateComponent } from './update/match-update.component';
import { MatchDeleteDialogComponent } from './delete/match-delete-dialog.component';
import { MatchRoutingModule } from './route/match-routing.module';
import { MatchResultModule } from 'app/entities/match-result/match-result.module';

@NgModule({
  imports: [SharedModule, MatchRoutingModule, MatchResultModule],
  declarations: [MatchComponent, MatchDetailComponent, MatchUpdateComponent, MatchDeleteDialogComponent],
  entryComponents: [MatchDeleteDialogComponent],
  exports: [MatchComponent, MatchDetailComponent],
})
export class MatchModule {}
