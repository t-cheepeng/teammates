import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBarModule } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { JoinState } from '../../../types/api-output';
import { TeammatesCommonModule } from '../teammates-common/teammates-common.module';
import { JoinStatePipe } from './join-state.pipe';
import { StudentListComponent } from './student-list.component';

describe('StudentListComponent', () => {
  let component: StudentListComponent;
  let fixture: ComponentFixture<StudentListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [StudentListComponent, JoinStatePipe],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        NgbModule,
        MatSnackBarModule,
        TeammatesCommonModule,
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should snap with default fields', () => {
    expect(fixture).toMatchSnapshot();
  });

  it('should snap with table head set to hidden', () => {
    component.isHideTableHead = true;
    fixture.detectChanges();
    expect(fixture).toMatchSnapshot();
  });

  it('should snap with some student list data', () => {
    component.students = [
      {
        name: 'tester',
        team: 'Team 1',
        email: 'tester@tester.com',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Benny Charles',
        team: 'Team 1',
        email: 'benny.c.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Alice Betsy',
        team: 'Team 1',
        email: 'alice.b.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Danny Engrid',
        team: 'Team 1',
        email: 'danny.e.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
    ];

    fixture.detectChanges();
    expect(fixture).toMatchSnapshot();
  });

  it('should snap with some student list data when not allowed to modify student for a specific section', () => {
    component.students = [
      {
        name: 'tester',
        team: 'Team 1',
        email: 'tester@tester.com',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: false,
      },
      {
        name: 'Benny Charles',
        team: 'Team 1',
        email: 'benny.c.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: false,
      },
      {
        name: 'Alice Betsy',
        team: 'Team 1',
        email: 'alice.b.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },

      {
        name: 'Danny Engrid',
        team: 'Team 1',
        email: 'danny.e.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
    ];

    fixture.detectChanges();
    expect(fixture).toMatchSnapshot();
  });

  it('should snap with enable remind button set to true and two students yet to join', () => {
    component.students = [
      {
        name: 'tester',
        team: 'Team 1',
        email: 'tester@tester.com',
        status: JoinState.NOT_JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Benny Charles',
        team: 'Team 1',
        email: 'benny.c.tmms@gmail.tmt',
        status: JoinState.NOT_JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Alice Betsy',
        team: 'Team 1',
        email: 'alice.b.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Danny Engrid',
        team: 'Team 1',
        email: 'danny.e.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
    ];

    component.enableRemindButton = true;

    fixture.detectChanges();
    expect(fixture).toMatchSnapshot();
  });

  it('should snap with enable remind button set to true, one student yet to join when not allowed to modify' +
      ' student', () => {
    component.students = [
      {
        name: 'tester',
        team: 'Team 1',
        email: 'tester@tester.com',
        status: JoinState.NOT_JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: false,
      },
      {
        name: 'Benny Charles',
        team: 'Team 1',
        email: 'benny.c.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Alice Betsy',
        team: 'Team 1',
        email: 'alice.b.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Danny Engrid',
        team: 'Team 1',
        email: 'danny.e.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
    ];

    component.enableRemindButton = true;

    fixture.detectChanges();
    expect(fixture).toMatchSnapshot();
  });

  it('should snap with some student list data and some students to hide', () => {
    component.students = [
      {
        name: 'tester',
        team: 'Team 1',
        email: 'tester@tester.com',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Benny Charles',
        team: 'Team 1',
        email: 'benny.c.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Alice Betsy',
        team: 'Team 1',
        email: 'alice.b.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
      {
        name: 'Danny Engrid',
        team: 'Team 1',
        email: 'danny.e.tmms@gmail.tmt',
        status: JoinState.JOINED,
        sectionName: 'Tutorial Group 2',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
    ];

    component.listOfStudentsToHide = [
      'alice.b.tmms@gmail.tmt',
      'tester@tester.com',
    ];

    fixture.detectChanges();
    expect(fixture).toMatchSnapshot();
  });

  it('should snap with some student list data with no sections', () => {
    component.students = [
      {
        name: 'tester',
        team: 'Team 1',
        email: 'tester@tester.com',
        status: JoinState.JOINED,
        sectionName: 'None',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
    ];

    fixture.detectChanges();
    expect(fixture).toMatchSnapshot();
  });

  it('should display "Send Invite" button when a student has not joined the course', () => {
    component.enableRemindButton = true;
    component.students = [
      {
        name: 'tester',
        team: 'Team 1',
        email: 'tester@tester.com',
        status: JoinState.NOT_JOINED,
        sectionName: 'Tutorial Group 1',
        isAllowedToViewStudentInSection: true,
        isAllowedToModifyStudent: true,
      },
    ];

    fixture.detectChanges();

    const buttons: any = fixture.nativeElement.querySelectorAll('button');
    const sendInviteButton: any = Array.from(buttons)
        .find((button: any) => button.firstChild.nodeValue === 'Send Invite');
    expect(sendInviteButton).toBeTruthy();
  });
});
