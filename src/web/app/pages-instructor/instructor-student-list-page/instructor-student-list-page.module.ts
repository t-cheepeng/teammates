import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoadingSpinnerModule } from '../../components/loading-spinner/loading-spinner.module';
import { InstructorStudentListPageComponent } from './instructor-student-list-page.component';
import { StudentListModule } from '../../components/student-list/student-list.module';

/**
 * Module for instructor student list page.
 */
@NgModule({
  declarations: [
    InstructorStudentListPageComponent,
  ],
  exports: [
    InstructorStudentListPageComponent,
  ],
  imports: [
    LoadingSpinnerModule,
    CommonModule,
    FormsModule,
    RouterModule,
    StudentListModule,
    NgbModule,
  ],
})
export class InstructorStudentListPageModule { }
