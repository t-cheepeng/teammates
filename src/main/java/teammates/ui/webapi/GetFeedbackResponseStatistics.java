package teammates.ui.webapi;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import teammates.common.datatransfer.attributes.FeedbackResponseStatisticAttributes;
import teammates.common.util.Const;
import teammates.ui.output.FeedbackResponseStatData;
import teammates.ui.output.FeedbackResponseStatsData;

/**
 * Gets all the new feedback response statistics.
 */
public class GetFeedbackResponseStatistics extends AdminOnlyAction {

    @Override
    JsonResult execute() {
        String startTimestampStr = getRequestParamValue(Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START);
        String endTimestampStr = getRequestParamValue(Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END);

        if (startTimestampStr == null || endTimestampStr == null) {
            return new JsonResult("Error: no start and end time", HttpStatus.SC_BAD_REQUEST);
        }

        long startTimestamp, endTimestamp;
        try {
            startTimestamp = Long.parseLong(startTimestampStr);
            endTimestamp = Long.parseLong(endTimestampStr);
        } catch (Exception e) {
            return new JsonResult("Error: start and end time must be numbers", HttpStatus.SC_BAD_REQUEST);
        }

        if (startTimestamp < 0 || endTimestamp < 0) {
            return new JsonResult("Error: start and end time must be non-negative", HttpStatus.SC_BAD_REQUEST);
        }

        Instant start = Instant.ofEpochMilli(startTimestamp);
        Instant end = Instant.ofEpochMilli(endTimestamp);

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
