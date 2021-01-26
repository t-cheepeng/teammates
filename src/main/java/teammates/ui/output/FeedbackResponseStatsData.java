package teammates.ui.output;

import java.util.ArrayList;
import java.util.List;

/**
 * The API output format of new feedback response data for a list of time points
 */
public class FeedbackResponseStatsData extends ApiOutput {

    private final List<FeedbackResponseStatData> feedbackResponseStats;

    public FeedbackResponseStatsData(List<FeedbackResponseStatData> feedbackResponseStats) {
        this.feedbackResponseStats = feedbackResponseStats;
    }

    public List<FeedbackResponseStatData> getFeedbackResponseStats() {
        return feedbackResponseStats;
    }
}
