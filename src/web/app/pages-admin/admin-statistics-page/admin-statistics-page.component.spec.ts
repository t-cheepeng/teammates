import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminStatisticsPageComponent } from './admin-statistics-page.component';
import { AdminStatisticsPageModule } from './admin-statistics-page.module';

describe('AdminStatisticsPageComponent', () => {
  let component: AdminStatisticsPageComponent;
  let fixture: ComponentFixture<AdminStatisticsPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdminStatisticsPageModule, HttpClientTestingModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminStatisticsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
