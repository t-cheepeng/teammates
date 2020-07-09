import { DOCUMENT } from '@angular/common';
import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PageScrollService } from 'ngx-page-scroll-core';
import {
  ContributionStatistics,
  FeedbackConstantSumQuestionDetails,
  FeedbackNumericalScaleResponseDetails,
  FeedbackSession,
  ResponseOutput,
} from '../../../../types/api-output';
import { CommentTableModel } from '../../../components/comment-box/comment-table/comment-table.component';
import {
  QuestionEditFormMode,
  QuestionEditFormModel,
} from '../../../components/question-edit-form/question-edit-form-model';
import { QuestionSubmissionFormModel,
} from '../../../components/question-submission-form/question-submission-form-model';
import { Response } from '../../../components/question-types/question-statistics/question-statistics';
import {
  QuestionTabModel,
} from '../../../pages-instructor/instructor-session-result-page/instructor-session-result-page.component';
import {
    InstructorSessionResultSectionType,
} from '../../../pages-instructor/instructor-session-result-page/instructor-session-result-section-type.enum';
import { InstructorHelpSectionComponent } from '../instructor-help-section.component';
import {
  EXAMPLE_CONTRIBUTION_STATISTICS,
  EXAMPLE_DISTRIBUTED_POINT_OPTION_MODEL,
  EXAMPLE_DISTRIBUTED_POINT_RECIPIENT_MODEL,
  EXAMPLE_DISTRIBUTE_POINT_OPTION_QUESTIONS,
  EXAMPLE_DISTRIBUTE_POINT_OPTION_QUESTION_DETAIL,
  EXAMPLE_DISTRIBUTE_POINT_OPTION_QUESTION_TAB_MODEL,
  EXAMPLE_DISTRIBUTE_POINT_OPTION_RESPONSE_OUTPUT,
  EXAMPLE_ESSAY_QUESTION_MODEL,
  EXAMPLE_FEEDBACK_SESSION,
  EXAMPLE_INSTRUCTOR_COMMENT_TABLE_MODEL,
  EXAMPLE_MCQ_QUESTION_WITHOUT_WEIGHTS_MODEL,
  EXAMPLE_MCQ_QUESTION_WITH_WEIGHTS_MODEL,
  EXAMPLE_NUMERICAL_SCALE_QUESTIONS,
  EXAMPLE_NUMERICAL_SCALE_QUESTION_MODEL,
  EXAMPLE_NUMERICAL_SCALE_QUESTION_TAB_MODEL,
  EXAMPLE_NUMERICAL_SCALE_RESPONSES,
  EXAMPLE_NUMERICAL_SCALE_RESPONSE_OUTPUT,
  EXAMPLE_RANK_OPTION_QUESTION_MODEL,
  EXAMPLE_RANK_RECIPIENT_QUESTIONS,
  EXAMPLE_RANK_RECIPIENT_QUESTION_MODEL,
  EXAMPLE_RANK_RECIPIENT_QUESTION_TAB_MODEL,
  EXAMPLE_RANK_RECIPIENT_RESPONSE_OUTPUT,
  EXAMPLE_RESPONDER_RUBRIC_SUBMISSION_FORM_MODEL,
  EXAMPLE_RUBRIC_QUESTION_MODEL,
  EXAMPLE_RUBRIC_QUESTION_QUESTIONS,
  EXAMPLE_RUBRIC_QUESTION_QUESTION_TAB_MODEL,
  EXAMPLE_RUBRIC_QUESTION_RESPONSE_OUTPUT,
  EXAMPLE_TEAM_CONTRIBUTION_QUESTIONS,
  EXAMPLE_TEAM_CONTRIBUTION_QUESTION_MODEL,
  EXAMPLE_TEAM_CONTRIBUTION_QUESTION_TAB_MODEL,
  EXAMPLE_TEAM_CONTRIBUTION_RESPONSE_OUTPUT,
} from './instructor-help-questions-data';

/**
 * Questions Section of the Instructor Help Page.
 */
@Component({
  selector: 'tm-instructor-help-questions-section',
  templateUrl: './instructor-help-questions-section.component.html',
  styleUrls: ['./instructor-help-questions-section.component.scss'],
})
export class InstructorHelpQuestionsSectionComponent extends InstructorHelpSectionComponent implements OnInit {

  // enum
  InstructorSessionResultSectionType: typeof InstructorSessionResultSectionType = InstructorSessionResultSectionType;
  QuestionEditFormMode: typeof QuestionEditFormMode = QuestionEditFormMode;

  readonly exampleEssayQuestionModel: QuestionEditFormModel = EXAMPLE_ESSAY_QUESTION_MODEL;
  readonly exampleNumericalScaleQuestionModel: QuestionEditFormModel = EXAMPLE_NUMERICAL_SCALE_QUESTION_MODEL;
  readonly exampleNumericalScaleResponses: Response<FeedbackNumericalScaleResponseDetails>[]
    = EXAMPLE_NUMERICAL_SCALE_RESPONSES;
  readonly exampleNumericalScaleResponseOutput: ResponseOutput[] = EXAMPLE_NUMERICAL_SCALE_RESPONSE_OUTPUT;
  readonly exampleNumericalScaleQuestionTabModel: QuestionTabModel = EXAMPLE_NUMERICAL_SCALE_QUESTION_TAB_MODEL;
  readonly exampleNumericalScaleQuestions: Record<string, QuestionTabModel> = EXAMPLE_NUMERICAL_SCALE_QUESTIONS;
  readonly exampleInstructorCommentTableModel: Record<string, CommentTableModel>
    = EXAMPLE_INSTRUCTOR_COMMENT_TABLE_MODEL;
  readonly exampleFeedbackSession: FeedbackSession = EXAMPLE_FEEDBACK_SESSION;
  readonly exampleDistributePointOptionQuestionDetail: FeedbackConstantSumQuestionDetails
    = EXAMPLE_DISTRIBUTE_POINT_OPTION_QUESTION_DETAIL;
  readonly exampleDistributedPointOptionModel: QuestionEditFormModel = EXAMPLE_DISTRIBUTED_POINT_OPTION_MODEL;
  readonly exampleDistributePointOptionResponseOutput: ResponseOutput[]
    = EXAMPLE_DISTRIBUTE_POINT_OPTION_RESPONSE_OUTPUT;
  readonly exampleDistributePointOptionQuestionTabModel: QuestionTabModel
    = EXAMPLE_DISTRIBUTE_POINT_OPTION_QUESTION_TAB_MODEL;
  readonly exampleDistributePointOptionQuestions: Record<string, QuestionTabModel>
    = EXAMPLE_DISTRIBUTE_POINT_OPTION_QUESTIONS;
  readonly exampleDistributedPointRecipientModel: QuestionEditFormModel = EXAMPLE_DISTRIBUTED_POINT_RECIPIENT_MODEL;
  readonly exampleTeamContributionQuestionModel: QuestionEditFormModel = EXAMPLE_TEAM_CONTRIBUTION_QUESTION_MODEL;
  readonly exampleTeamContributionResponseOutput: ResponseOutput[] = EXAMPLE_TEAM_CONTRIBUTION_RESPONSE_OUTPUT;
  readonly exampleContributionStatistics: ContributionStatistics = EXAMPLE_CONTRIBUTION_STATISTICS;
  readonly exampleTeamContributionQuestionTabModel: QuestionTabModel = EXAMPLE_TEAM_CONTRIBUTION_QUESTION_TAB_MODEL;
  readonly exampleTeamContributionQuestions: Record<string, QuestionTabModel> = EXAMPLE_TEAM_CONTRIBUTION_QUESTIONS;
  readonly exampleRubricQuestionModel: QuestionEditFormModel = EXAMPLE_RUBRIC_QUESTION_MODEL;
  readonly exampleRubricQuestionResponseOutput: ResponseOutput[] = EXAMPLE_RUBRIC_QUESTION_RESPONSE_OUTPUT;
  readonly exampleRubricQuestionQuestionTabModel: QuestionTabModel = EXAMPLE_RUBRIC_QUESTION_QUESTION_TAB_MODEL;
  readonly exampleRubricQuestionQuestions: Record<string, QuestionTabModel> = EXAMPLE_RUBRIC_QUESTION_QUESTIONS;
  readonly exampleRankRecipientQuestionModel: QuestionEditFormModel = EXAMPLE_RANK_RECIPIENT_QUESTION_MODEL;
  readonly exampleRankRecipientResponseOutput: ResponseOutput[] = EXAMPLE_RANK_RECIPIENT_RESPONSE_OUTPUT;
  readonly exampleRankRecipientQuestionTabModel: QuestionTabModel = EXAMPLE_RANK_RECIPIENT_QUESTION_TAB_MODEL;
  readonly exampleRankRecipientQuestions: Record<string, QuestionTabModel> = EXAMPLE_RANK_RECIPIENT_QUESTIONS;
  readonly exampleRankOptionQuestionModel: QuestionEditFormModel = EXAMPLE_RANK_OPTION_QUESTION_MODEL;
  readonly exampleMCQQuestionWithoutWeightsModel: QuestionEditFormModel = EXAMPLE_MCQ_QUESTION_WITHOUT_WEIGHTS_MODEL;
  readonly exampleMCQQuestionWithWeightsModel: QuestionEditFormModel = EXAMPLE_MCQ_QUESTION_WITH_WEIGHTS_MODEL;
  readonly exampleResponderRubricSubmissionFormModel: QuestionSubmissionFormModel
    = EXAMPLE_RESPONDER_RUBRIC_SUBMISSION_FORM_MODEL;

  isEssayQuestionsCollapsed: boolean = false;
  isMCQSingleAnsCollapsed: boolean = false;
  isMCQMultipleAnsCollapsed: boolean = false;
  isNumericalScaleCollapsed: boolean = false;
  isPointsOptionsCollapsed: boolean = false;
  isPointsRecipientsCollapsed: boolean = false;
  isContributionQsCollapsed: boolean = false;
  isRubricQsCollapsed: boolean = false;
  isRankOptionsCollapsed: boolean = false;
  isRankRecipientsCollapsed: boolean = false;
  @Output() collapsePeerEvalTips: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private modalService: NgbModal,
              private pageScrollService: PageScrollService,
              @Inject(DOCUMENT) private document: any) {
    super();
  }

  /**
   * Opens modal window.
   */
  openModal(modal: any): void {
    this.modalService.open(modal);
  }

  ngOnInit(): void {
  }

  /**
   * Scrolls to an HTML element with a given target id.
   */
  jumpTo(target: string): boolean {
    this.pageScrollService.scroll({
      document: this.document,
      scrollTarget: `#${target}`,
      scrollOffset: 70,
    });
    if (target === 'tips-for-conducting-peer-eval') {
      this.collapsePeerEvalTips.emit(true);
    }
    return false;
  }
}
