import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AdminStatisticsPageComponent } from './admin-statistics-page.component';
import { NgApexchartsModule } from 'ng-apexcharts';
import { LoadingSpinnerModule } from '../../components/loading-spinner/loading-spinner.module';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

const routes: Routes = [
  {
    path: '',
    component: AdminStatisticsPageComponent,
  },
];

/**
 * Module for admin statistics page.
 */
@NgModule({
  declarations: [AdminStatisticsPageComponent],
  exports: [
      AdminStatisticsPageComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    NgApexchartsModule,
    LoadingSpinnerModule,
    NgbDatepickerModule,
    FormsModule
  ],
})
export class AdminStatisticsPageModule { }
