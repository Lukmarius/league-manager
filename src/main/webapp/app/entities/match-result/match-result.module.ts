import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MatchResultComponent } from './list/match-result.component';
import { MatchResultDetailComponent } from './detail/match-result-detail.component';
import { MatchResultUpdateComponent } from './update/match-result-update.component';
import { MatchResultDeleteDialogComponent } from './delete/match-result-delete-dialog.component';
import { MatchResultRoutingModule } from './route/match-result-routing.module';

@NgModule({
  imports: [SharedModule, MatchResultRoutingModule],
  declarations: [MatchResultComponent, MatchResultDetailComponent, MatchResultUpdateComponent, MatchResultDeleteDialogComponent],
  entryComponents: [MatchResultDeleteDialogComponent],
})
export class MatchResultModule {}
