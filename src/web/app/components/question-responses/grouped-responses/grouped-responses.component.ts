import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  CommentOutput,
  FeedbackSession, FeedbackSessionPublishStatus, FeedbackSessionSubmissionStatus,
  QuestionOutput,
  ResponseVisibleSetting,
  SessionVisibleSetting,
} from '../../../../types/api-output';
import { CommentRowMode, CommentRowModel } from '../../comment-box/comment-row/comment-row.component';
import { ResponsesInstructorCommentsBase } from '../responses-instructor-comments-base';

/**
 * A list of responses grouped in GRQ/RGQ mode.
 */
@Component({
  selector: 'tm-grouped-responses',
  templateUrl: './grouped-responses.component.html',
  styleUrls: ['./grouped-responses.component.scss'],
})
export class GroupedResponsesComponent extends ResponsesInstructorCommentsBase implements OnInit {

  // enum
  CommentRowMode: typeof CommentRowMode = CommentRowMode;

  @Input() responses: QuestionOutput[] = [];
  @Input() userToPhotoUrl: Record<string, string> = {};

  @Input() isGrq: boolean = true;
  @Input() session: FeedbackSession = {
    courseId: '',
    timeZone: '',
    feedbackSessionName: '',
    instructions: '',
    submissionStartTimestamp: 0,
    submissionEndTimestamp: 0,
    gracePeriod: 0,
    sessionVisibleSetting: SessionVisibleSetting.AT_OPEN,
    responseVisibleSetting: ResponseVisibleSetting.AT_VISIBLE,
    submissionStatus: FeedbackSessionSubmissionStatus.OPEN,
    publishStatus: FeedbackSessionPublishStatus.NOT_PUBLISHED,
    isClosingEmailEnabled: true,
    isPublishedEmailEnabled: true,
    createdAtTimestamp: 0,
  };

  @Output()
  loadPhotoEvent: EventEmitter<string> = new EventEmitter();

  constructor() {
    super();
  }

  ngOnInit(): void {
  }

  get teamInfo(): Record<string, string> {
    const team: Record<string, string> = {};
    team.recipient =  this.responses[0].allResponses[0].recipientTeam !== '' ?
        `(${this.responses[0].allResponses[0].recipientTeam})` : '';
    team.giver = `(${this.responses[0].allResponses[0].giverTeam})`;
    return team;
  }

  get topName(): string {
    return this.responses[0].allResponses[0][this.isGrq ? 'recipient' : 'giver'];
  }

  get bottomName(): string {
    return this.responses[0].allResponses[0][this.isGrq ? 'giver' : 'recipient'];
  }

  /**
   * Transforms participant comment to comment row model.
   */
  transformParticipantCommentToCommandRowModel(participantComment: CommentOutput): CommentRowModel {
    return {
      originalComment: participantComment,
      timezone: this.session.timeZone,
      commentGiverName: participantComment.commentGiverName,
      lastEditorName: participantComment.lastEditorName,
      commentEditFormModel: {
        commentText: participantComment.commentText,
        isUsingCustomVisibilities: false,
        showCommentTo: [],
        showGiverNameTo: [],
      },
      isEditing: false,
    };
  }

}
