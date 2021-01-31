package teammates.ui.webapi;

import java.time.Instant;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import teammates.common.util.Const;
import teammates.ui.output.FeedbackResponseStatsData;

/**
 * SUT: {@link GetFeedbackResponseStatistics}.
 */
public class GetFeedbackResponseStatisticsTest extends BaseActionTest<GetFeedbackResponseStatistics> {

    private static Instant jan1 = Instant.parse("2020-01-01T00:00:00.00Z");
    private static Instant jan2 = Instant.parse("2020-01-02T00:00:00.00Z");
    private static Instant jan3 = Instant.parse("2020-01-03T00:00:00.00Z");
    private static Instant jan4 = Instant.parse("2020-01-04T00:00:00.00Z");
    private static Instant jan5 = Instant.parse("2020-01-05T00:00:00.00Z");
    private static Instant jan6 = Instant.parse("2020-01-06T00:00:00.00Z");

    @Override
    protected String getActionUri() {
        return Const.ResourceURIs.RESPONSES_STATISTICS;
    }

    @Override
    protected String getRequestMethod() {
        return GET;
    }

    @Test
    @Override
    protected void testExecute() throws Exception {
        //See test cases below
    }

    @Test
    protected void testExecute_typicalUsage_shouldPass() throws Exception {
        loginAsAdmin();
        logic.setFeedbackResponseStatistic(jan1, 10);
        logic.setFeedbackResponseStatistic(jan2, 12);
        logic.setFeedbackResponseStatistic(jan3, 17);
        logic.setFeedbackResponseStatistic(jan4, 22);
        logic.setFeedbackResponseStatistic(jan5, 24);

        ______TS("Typical usage");

        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START,
                String.valueOf(jan1.getEpochSecond() * 1000),
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END,
                String.valueOf(jan4.getEpochSecond() * 1000),
        };
        GetFeedbackResponseStatistics getFeedbackResponseStatistics = getAction(submissionParams);
        JsonResult response = getJsonResult(getFeedbackResponseStatistics);
        FeedbackResponseStatsData feedbackResponseStatsData = (FeedbackResponseStatsData) response.getOutput();

        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        assertEquals(3, feedbackResponseStatsData.getFeedbackResponseStats().size());

        submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START,
                String.valueOf(jan1.getEpochSecond() * 1000),
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END,
                String.valueOf(jan6.getEpochSecond() * 1000),
        };
        getFeedbackResponseStatistics = getAction(submissionParams);
        response = getJsonResult(getFeedbackResponseStatistics);
        feedbackResponseStatsData = (FeedbackResponseStatsData) response.getOutput();

        assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        assertEquals(5, feedbackResponseStatsData.getFeedbackResponseStats().size());
    }

    @Test
    protected void testExecute_notEnoughParameters_shouldFail() {
        loginAsAdmin();

        ______TS("Not enough parameters");

        String[] submissionParams = new String[] {};
        GetFeedbackResponseStatistics getFeedbackResponseStatistics = getAction(submissionParams);
        JsonResult response = getJsonResult(getFeedbackResponseStatistics);

        assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST);

        submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "1000",
        };
        getFeedbackResponseStatistics = getAction(submissionParams);
        response = getJsonResult(getFeedbackResponseStatistics);

        assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    protected void testExecute_invalidParameters_shouldFail() {
        loginAsAdmin();

        ______TS("Invalid parameter types: not number");

        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "thousand",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "two thousand",
        };
        GetFeedbackResponseStatistics getFeedbackResponseStatistics = getAction(submissionParams);
        JsonResult response = getJsonResult(getFeedbackResponseStatistics);

        assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST);

        ______TS("Invalid parameter types: negative numbers");

        submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "-300",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "-700",
        };
        getFeedbackResponseStatistics = getAction(submissionParams);
        response = getJsonResult(getFeedbackResponseStatistics);

        assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST);

        ______TS("Invalid parameter types: start is after end");

        submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "1000",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "500",
        };
        getFeedbackResponseStatistics = getAction(submissionParams);
        response = getJsonResult(getFeedbackResponseStatistics);

        assertEquals(response.getStatusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @Override
    protected void testAccessControl() throws Exception {
        //see test cases below
    }

    @Test
    protected void testAccessControl_testAdminAccess_shouldPass() {
        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "1000",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "2000",
        };

        verifyAccessibleForAdmin(submissionParams);
    }

    @Test
    protected void testAccessControl_testInstructorAccess_shouldFail() {
        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "1000",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "2000",
        };

        verifyInaccessibleForInstructors(submissionParams);
        verifyCannotAccess(submissionParams);
    }

    @Test
    protected void testAccessControl_testStudentAccess_shouldFail() {
        String[] submissionParams = new String[] {
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_START, "1000",
                Const.ParamsNames.FEEDBACK_RESPONSE_STATISTICS_END, "2000",
        };

        verifyInaccessibleForStudents(submissionParams);
        verifyCannotAccess(submissionParams);
    }

}
