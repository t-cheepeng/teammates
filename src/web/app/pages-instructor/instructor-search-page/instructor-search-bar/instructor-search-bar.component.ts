import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

/**
 * Parameters inputted by user to be used in search
 */
export interface SearchParams {
  searchKey: string;
  isSearchForStudents: boolean;
  isSearchForComments: boolean;
}

/**
 * Search bar on instructor search page
 */
@Component({
  selector: 'tm-instructor-search-bar',
  templateUrl: './instructor-search-bar.component.html',
  styleUrls: ['./instructor-search-bar.component.scss'],
})
export class InstructorSearchBarComponent implements OnInit {

  @Input() searchParams: SearchParams = {
    searchKey: '',
    isSearchForStudents: true,
    isSearchForComments: false
  }

  @Output() searched: EventEmitter<SearchParams> = new EventEmitter();

  @Output() searchParamsChange: EventEmitter<SearchParams> = new EventEmitter();

  constructor() {}

  ngOnInit(): void {}

  /**
   * send the search data to parent for processing
   */
  search(): void {
    this.searched.emit(this.searchParams);
  }
}
