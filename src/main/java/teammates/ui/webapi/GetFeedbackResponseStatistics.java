package teammates.ui.webapi;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import teammates.common.datatransfer.attributes.FeedbackResponseStatisticAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Const;
import teammates.ui.output.FeedbackResponseStatData;
import teammates.ui.output.FeedbackResponseStatsData;
import teammates.storage.entity.FeedbackResponseStatistic;

public class GetFeedbackResponseStatistics extends AdminOnlyAction {

    @Override
    JsonResult execute() {
        String startTimestampStr = getRequestParamValue(Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START);
        String endTimestampStr = getRequestParamValue(Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END);

        if (startTimestampStr ==  null || endTimestampStr == null) {
            return new JsonResult("Error: no start and end time", HttpStatus.SC_BAD_REQUEST);
        }

        // TODO: Error handling, if the types are wrong (not long), also ensure that start <= end
        long startTimestamp = Long.parseLong(startTimestampStr);
        long endTimestamp = Long.parseLong(endTimestampStr);
        Instant start = Instant.ofEpochMilli(startTimestamp);
        Instant end = Instant.ofEpochMilli(endTimestamp);

        // TODO: this is for testing create only, delete this later
        try {
            logic.createFeedbackResponseStatistic(Instant.now());
        } catch (Exception e) {
            System.out.println("@@@ PROBLEM CREATING FEEDBACK RESPONSES STATISTIC @@@");
            System.out.println(e);
        }

        List<FeedbackResponseStatisticAttributes> statsAttributes = logic.getFeedbackResponseStatistics(start, end);

        List<FeedbackResponseStatData> statsData = new ArrayList<>();
        statsAttributes.forEach(statAttribute -> {
            long responseTimeStamp = statAttribute.getTime().toEpochMilli();
            int count = statAttribute.getCount();
            FeedbackResponseStatData statData = new FeedbackResponseStatData(responseTimeStamp, count);
            statsData.add(statData);
        });
        FeedbackResponseStatsData result = new FeedbackResponseStatsData(statsData);
        return new JsonResult(result);
    }
}
