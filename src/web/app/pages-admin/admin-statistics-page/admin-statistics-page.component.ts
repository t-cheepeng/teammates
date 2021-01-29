import { Component, OnInit } from '@angular/core';
import { ApexTitleSubtitle, ApexXAxis, ApexYAxis, ApexAxisChartSeries, ApexChart, ApexTooltip } from 'ng-apexcharts';
import { FeedbackResponsesService } from '../../../services/feedback-responses.service';
import { finalize } from 'rxjs/operators';
import { FeedbackResponseStat, FeedbackResponseStats } from '../../../types/api-output';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

interface ChartOptions {
  series: ApexAxisChartSeries,
  chart: ApexChart,
  xaxis: ApexXAxis,
  yaxis: ApexYAxis,
  title: ApexTitleSubtitle,
  tooltip: ApexTooltip,
}

@Component({
  selector: 'tm-admin-statistics-page',
  templateUrl: './admin-statistics-page.component.html',
  styleUrls: ['./admin-statistics-page.component.scss']
})
export class AdminStatisticsPageComponent implements OnInit {
  // Update interval for getting new feedback response in ms
  private readonly UPDATE_INTERVAL: number = 60000;
  private readonly TITLE: string = 'New Feedback Responses';

  isLoadingStatistics = false;
  numOfUpdates: number = 0;

  startDate: NgbDateStruct = {
    day: 0, month: 0, year: 0
  };
  endDate: NgbDateStruct = {
    day: 0, month: 0, year: 0
  };

  intervalHandler?: number;
  chartOptions: ChartOptions = {
    series: [
      {
        name: this.TITLE,
        data: [],
      }
    ],
    chart: {
      height: 350,
      type: 'line',
    },
    title: {
      text: this.TITLE,
    },
    xaxis: {
      type: 'datetime',
    },
    yaxis: {
      title: {
        text: 'Number'
      },
    },
    tooltip: {
      x: {
        format: 'dd MMM, HH:mm:ss'
      }
    }
  };

  constructor(private feedbackResponseService: FeedbackResponsesService) {
  }

  ngOnInit(): void {

  }

  retrieveStatistics(): void {
    const start: number = this.ngbDateToDate(this.startDate).getTime();
    const end: number = this.ngbDateToDate(this.endDate).getTime();
    this.numOfUpdates = 0;

    if (this.intervalHandler) {
      window.clearInterval(this.intervalHandler);
    }

    this.isLoadingStatistics = true;
    this.feedbackResponseService
        .getFeedbackResponseStatistics(start.toString(), end.toString())
        .pipe(finalize(() => this.isLoadingStatistics = false))
        .subscribe((statistics: FeedbackResponseStats) => {
          this.toSeriesData(statistics);

          this.intervalHandler = window.setInterval(() => {
            this.updateStatistics();
          }, this.UPDATE_INTERVAL);
        });
  }

  updateStatistics(): void {
    const start: number = this.ngbDateToDate(this.startDate).getTime();
    const end: number = this.ngbDateToDate(this.endDate).getTime() + (this.numOfUpdates * this.UPDATE_INTERVAL);
    this.numOfUpdates++;

    this.feedbackResponseService
        .getFeedbackResponseStatistics(start.toString(), end.toString())
        .subscribe((statistics: FeedbackResponseStats) => {
          this.toSeriesData(statistics);
        });
  }

  private toSeriesData(statistics: FeedbackResponseStats) {
    this.chartOptions.series = [
      {
        name: this.TITLE,
        data: statistics.feedbackResponseStats
            .map((statistic: FeedbackResponseStat) => {
              return {
                x: statistic.responseTimeStamp,
                y: statistic.number
              };
            }),
      }
    ];

  }

  private ngbDateToDate(date: NgbDateStruct): Date {
    return new Date(date.year, date.month - 1, date.day);
  }
}
