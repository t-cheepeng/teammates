package teammates.ui.output;

/**
 * The API output format of new feedback response data in singular point of time.
 */
public class FeedbackResponseStatData extends ApiOutput {

    private final long responseTimeStamp;

    private final int number;

    public FeedbackResponseStatData() {
        this.responseTimeStamp = 0;
        this.number = 0;
        // TODO: Create new attributes or reuse existing attribute
    }

    public long getResponseTimeStamp() {
        return responseTimeStamp;
    }

    public int getNumber() {
        return number;
    }
}
