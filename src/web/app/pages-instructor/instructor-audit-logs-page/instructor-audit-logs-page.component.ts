import { Component, OnInit } from '@angular/core';
import { concatMap, finalize, map, mergeAll } from 'rxjs/operators';
import moment from 'moment-timezone';
import { CourseService } from '../../../services/course.service';
import { StatusMessageService } from '../../../services/status-message.service';
import { StudentService } from '../../../services/student.service';
import {
  Course,
  Courses,
  FeedbackSessionLog, FeedbackSessionLogEntry,
  FeedbackSessionLogs,
  Student,
  Students
} from '../../../types/api-output';
import { DateFormat } from '../../components/session-edit-form/session-edit-form-model';
import { TimeFormat } from '../../components/session-edit-form/time-picker/time-picker.component';
import { ErrorMessageOutput } from '../../error-message-output';
import { LogService } from '../../../services/log.service';
import { LOCAL_DATE_TIME_FORMAT, TimeResolvingResult, TimezoneService } from '../../../services/timezone.service';
import { forkJoin, Observable } from 'rxjs';
import { LogTypes } from '../../../types/api-const';
import { ColumnData, SortableTableCellData } from '../../components/sortable-table/sortable-table.component';
import { SortBy } from '../../../types/sort-properties';

/**
 * Model for searching of logs
 */
interface SearchLogsFormModel {
  logsDateFrom: DateFormat;
  logsDateTo: DateFormat;
  logsTimeFrom: TimeFormat;
  logsTimeTo: TimeFormat;
  courseId: string;
  studentName: string;
}

/**
 * Model for displaying of feedback session logs
 */
interface FeedbackSessionLogModel {
  feedbackSessionName: string;
  logColumnsData: ColumnData[];
  logRowsData: SortableTableCellData[][];
  isTabExpanded: boolean;
}

/**
 * Component for instructor logs
 */
@Component({
  selector: 'tm-instructor-audit-logs-page',
  templateUrl: './instructor-audit-logs-page.component.html',
  styleUrls: ['./instructor-audit-logs-page.component.scss'],
})
export class InstructorAuditLogsPageComponent implements OnInit {

  // enum
  SortBy: typeof SortBy = SortBy;

  formModel: SearchLogsFormModel = {
    logsDateFrom: { year: 0, month: 0, day: 0 },
    logsTimeFrom: { hour: 0, minute: 0 },
    logsDateTo: { year: 0, month: 0, day: 0 },
    logsTimeTo: { hour: 0, minute: 0 },
    courseId: '',
    studentName: '',
  };
  courses: Course[] = [];
  courseToStudents: Record<string, Student[]> = {};
  searchResults: FeedbackSessionLogModel[] = [];
  isLoading: boolean = true;
  isSearching: boolean = false;

  constructor(private courseService: CourseService,
              private studentService: StudentService,
              private logsService: LogService,
              private timezoneService: TimezoneService,
              private statusMessageService: StatusMessageService) { }

  ngOnInit(): void {
    this.loadData();
  }

  /**
   * Search for logs of student activity
   */
  search(): void {
    this.isSearching = true;
    this.searchResults = [];
    const localDateTime: Observable<number>[] = [
      this.resolveLocalDateTime(this.formModel.logsDateFrom, this.formModel.logsTimeFrom, 'Search period from'),
      this.resolveLocalDateTime(this.formModel.logsDateTo, this.formModel.logsTimeTo, 'Search period until'),
    ];
    forkJoin(localDateTime)
        .pipe(
            concatMap((timestamp: number[]) => {
                return this.logsService.searchFeedbackSessionLog({
                  courseId: this.formModel.courseId,
                  searchFrom: timestamp[0].toString(),
                  searchUntil: timestamp[1].toString()
                })}),
            finalize(() => this.isSearching = false))
        .subscribe((logs: FeedbackSessionLogs) => {
          logs.feedbackSessionLogs.map((log: FeedbackSessionLog) => this.searchResults.push(this.toFeedbackSessionLogModel(log)))
        }, (e: ErrorMessageOutput) => this.statusMessageService.showErrorToast(e.error.message));

    //TODO: For testing only
    // this.populateFake();
    // console.log(this.searchResults);
  }

  /**
   * Load all courses and students that the instructor have
   */
  private loadData(): void {
    const emptyStudent: Student = {
      courseId: '', email: '', name: '', sectionName: '', teamName: '',
    };
    this.courseService
        .getAllCoursesAsInstructor('active')
        .pipe(
            concatMap((courses: Courses) => courses.courses.map((course: Course) => {
              this.courses.push(course);
              return this.studentService.getStudentsFromCourse({ courseId: course.courseId });
            })),
            mergeAll(),
            finalize(() => this.isLoading = false))
        .subscribe(((student: Students) =>
                // Student with no name is selectable to search for all students since the field is optional
                this.courseToStudents[student.students[0].courseId] = [emptyStudent, ...student.students]),
            (e: ErrorMessageOutput) => this.statusMessageService.showErrorToast(e.error.message));
  }

  private resolveLocalDateTime(date: DateFormat, time: TimeFormat, fieldName: string): Observable<number> {
    const inst: any = moment();
    inst.set('year', date.year);
    inst.set('month', date.month - 1); // moment month is from 0-11
    inst.set('date', date.day);
    inst.set('hour', time.hour);
    inst.set('minute', time.minute);
    const localDateTime: string = inst.format(LOCAL_DATE_TIME_FORMAT);
    const course: Course | undefined = this.courses.find((course: Course) => course.courseId = this.formModel.courseId);

    let timeZone: string = '';
    if (course) {
      timeZone = course.timeZone;
    }

    return this.timezoneService.getResolvedTimestamp(localDateTime, timeZone, fieldName)
        .pipe(map((result: TimeResolvingResult) => result.timestamp));
  }

  private toFeedbackSessionLogModel(log: FeedbackSessionLog): FeedbackSessionLogModel {
    return {
      isTabExpanded: false,
      feedbackSessionName: log.feedbackSessionData.feedbackSessionName,
      logColumnsData: [
        { header: 'Activity Time', sortBy: SortBy.LOG_DATE },
        { header: 'Name', sortBy: SortBy.GIVER_NAME },
        { header: 'Activity', sortBy: SortBy.LOG_TYPE },
        { header: 'Email', sortBy: SortBy.RESPONDENT_EMAIL },
        { header: 'Section', sortBy: SortBy.SECTION_NAME },
        { header: 'Team', sortBy: SortBy.TEAM_NAME },
      ],
      logRowsData: log.feedbackSessionLogEntries.map((entry: FeedbackSessionLogEntry) => {
        return [
          { value: this.timezoneService.formatToString(entry.timestamp, log.feedbackSessionData.timeZone, 'ddd, DD MMM, YYYY hh:mm A') },
          { value: entry.studentData.name },
          { value: entry.feedbackSessionLogType == LogTypes.FEEDBACK_SESSION_ACCESS ? "Viewed the submission page" : "Submitted response"},
          { value: entry.studentData.email },
          { value: entry.studentData.sectionName },
          { value: entry.studentData.teamName },
        ];
      }),
    };
  }

  //TODO: Delete this for testing
  // @ts-ignore
  private populateFake(): void {
    this.searchResults = [
      {
        feedbackSessionName: 'Feedback Session Name 1',
        isTabExpanded: false,
        logColumnsData: [
          { header: 'Activity Time', sortBy: SortBy.LOG_DATE },
          { header: 'Name', sortBy: SortBy.GIVER_NAME },
          { header: 'Activity', sortBy: SortBy.LOG_TYPE },
          { header: 'Email', sortBy: SortBy.RESPONDENT_EMAIL },
          { header: 'Section', sortBy: SortBy.SECTION_NAME },
          { header: 'Team', sortBy: SortBy.TEAM_NAME },
        ],
        logRowsData: [
            [
              { value: this.timezoneService.formatToString(1615974162000, 'Asia/Singapore', 'ddd, DD MMM, YYYY hh:mm A') },
              { value: 'test' },
              { value: 'Viewed the submission page' },
              { value: 'test@example.com' },
              { value: 'testsection' },
              { value: 'testteam' },
            ],
          [
            { value: this.timezoneService.formatToString(1615977762000, 'Asia/Singapore', 'ddd, DD MMM, YYYY hh:mm A') },
            { value: 'test2' },
            { value: 'Submitted response' },
            { value: 'test2@example.com' },
            { value: 'testsection' },
            { value: 'testteam' },
          ],
        ]
      },
      {
        feedbackSessionName: 'Feedback Session Name 2',
        isTabExpanded: false,
        logColumnsData: [
          { header: 'Activity Time', sortBy: SortBy.LOG_DATE },
          { header: 'Name', sortBy: SortBy.GIVER_NAME },
          { header: 'Activity', sortBy: SortBy.LOG_TYPE },
          { header: 'Email', sortBy: SortBy.RESPONDENT_EMAIL },
          { header: 'Section', sortBy: SortBy.SECTION_NAME },
          { header: 'Team', sortBy: SortBy.TEAM_NAME },
        ],
        logRowsData: [
          [
            { value: this.timezoneService.formatToString(1615801362000, 'Asia/Singapore', 'ddd, DD MMM, YYYY hh:mm A') },
            { value: 'test3' },
            { value: 'Viewed the submission page' },
            { value: 'test3@example.com' },
            { value: 'testsection' },
            { value: 'testteam' },
          ],
          [
            { value: this.timezoneService.formatToString(1615801962000, 'Asia/Singapore', 'ddd, DD MMM, YYYY hh:mm A') },
            { value: 'test4' },
            { value: 'Submitted response' },
            { value: 'test4@example.com' },
            { value: 'testsection' },
            { value: 'testteam' },
          ],
        ]
      },
    ];
  }

}
