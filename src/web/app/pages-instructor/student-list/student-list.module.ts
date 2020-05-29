import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { JoinStatePipe } from './join-state.pipe';
import { StudentListComponent } from './student-list.component';
import { TeammatesCommonModule } from "../../components/teammates-common/teammates-common.module";

/**
 * Module for student list table component.
 */
@NgModule({
  declarations: [
    JoinStatePipe,
    StudentListComponent,
  ],
  exports: [
    StudentListComponent,
  ],
  imports: [
    CommonModule,
    NgbModule,
    RouterModule,
    TeammatesCommonModule,
  ],
})
export class StudentListModule { }
