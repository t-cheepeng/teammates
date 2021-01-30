import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { NgApexchartsModule } from 'ng-apexcharts';
import { LoadingSpinnerModule } from '../../components/loading-spinner/loading-spinner.module';
import { AdminStatisticsPageComponent } from './admin-statistics-page.component';

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
    FormsModule,
  ],
})
export class AdminStatisticsPageModule { }
