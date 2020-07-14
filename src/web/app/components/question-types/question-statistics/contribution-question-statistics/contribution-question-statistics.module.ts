import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ContributionRatingsListComponent } from './contribution-ratings-list.component';
import { ContributionComponent } from './contribution.component';

/**
 * Module for contribution question statistics
 */
@NgModule({
  declarations: [ContributionComponent, ContributionRatingsListComponent],
  imports: [
    CommonModule,
  ],
  entryComponents: [
    ContributionComponent, ContributionRatingsListComponent,
  ],
  exports: [
    ContributionComponent, ContributionRatingsListComponent,
  ],
})
export class ContributionQuestionStatisticsModule { }
