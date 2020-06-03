import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { StudentListModule } from '../student-list/student-list.module';
import { InstructorSearchBarComponent } from './instructor-search-bar/instructor-search-bar.component';
import { InstructorSearchPageComponent } from './instructor-search-page.component';
import { StudentResultTableComponent } from './student-result-table/student-result-table.component';
import { CommentResultTableComponent } from "./comment-result-table/comment-result-table.component";
import { CommentBoxModule } from "../../components/comment-box/comment-box.module";

/**
 * Module for instructor search page.
 */
@NgModule({
  declarations: [
    InstructorSearchPageComponent,
    InstructorSearchBarComponent,
    StudentResultTableComponent,
    CommentResultTableComponent,
  ],
  exports: [
    InstructorSearchPageComponent,
    InstructorSearchBarComponent,
    StudentResultTableComponent,
    CommentResultTableComponent,
  ],
  imports: [
    CommonModule,
    StudentListModule,
    FormsModule,
    RouterModule,
    NgbModule,
    CommentBoxModule,
  ],
})
export class InstructorSearchPageModule { }
