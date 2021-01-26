package teammates.ui.output;

/**
 * The API output format of new feedback response data in singular point of time.
 */
public class FeedbackResponseStatData extends ApiOutput {

    private final long responseTimeStamp;

    private final int number;

    public FeedbackResponseStatData(long responseTimeStamp, int number) {
        this.responseTimeStamp = responseTimeStamp;
        this.number = number;
    }

    public long getResponseTimeStamp() {
        return responseTimeStamp;
    }

    public int getNumber() {
        return number;
    }
}
