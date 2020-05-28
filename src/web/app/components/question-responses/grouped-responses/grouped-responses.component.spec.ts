import { async, ComponentFixture, TestBed } from '@angular/core/testing';

// tslint:disable-next-line:max-line-length
import { ResponseModerationButtonModule } from '../../../pages-instructor/instructor-session-result-page/response-moderation-button/response-moderation-button.module';
import { ViewPhotoPopoverModule } from '../../../pages-instructor/view-photo-popover/view-photo-popover.module';
import { CommentBoxModule } from '../../comment-box/comment-box.module';
import { QuestionTextWithInfoModule } from '../../question-text-with-info/question-text-with-info.module';
import { SingleResponseModule } from '../single-response/single-response.module';
import { GroupedResponsesComponent } from './grouped-responses.component';

describe('GroupedResponsesComponent', () => {
  let component: GroupedResponsesComponent;
  let fixture: ComponentFixture<GroupedResponsesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GroupedResponsesComponent],
      imports: [
        QuestionTextWithInfoModule,
        SingleResponseModule,
        CommentBoxModule,
        ResponseModerationButtonModule,
        ViewPhotoPopoverModule,
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupedResponsesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
