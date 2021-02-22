package teammates.logic.core;

import java.time.Instant;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.FeedbackResponseStatisticAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.InvalidParametersException;
import teammates.storage.api.FeedbackResponseStatisticsDb;
import teammates.storage.entity.FeedbackResponseStatistic;

/**
 * SUT: {@link FeedbackResponseStatisticsLogic}.
 */
public class FeedbackResponseStatisticsLogicTest extends BaseLogicTest {
    private static FeedbackResponseStatisticsLogic frsLogic = FeedbackResponseStatisticsLogic.inst();
    private static FeedbackResponseStatisticsDb frsDb = new FeedbackResponseStatisticsDb();
    private static Instant jan1 = Instant.parse("2020-01-01T00:00:00.00Z");
    private static Instant jan2 = Instant.parse("2020-01-02T00:00:00.00Z");
    private static Instant jan3 = Instant.parse("2020-01-03T00:00:00.00Z");
    private static Instant jan4 = Instant.parse("2020-01-04T00:00:00.00Z");
    private static Instant jan5 = Instant.parse("2020-01-05T00:00:00.00Z");
    private static Instant end = Instant.parse("2020-12-31T23:59:59.00Z");

    @Override
    protected void prepareTestData() {
        // see beforeMethod()
    }

    @BeforeMethod
    public void beforeMethod() throws InvalidParametersException, EntityAlreadyExistsException {
        FeedbackResponseStatistic frs1 = new FeedbackResponseStatistic(jan1, 1, 1);
        FeedbackResponseStatistic frs2 = new FeedbackResponseStatistic(jan2, 4, 5);
        FeedbackResponseStatistic frs3 = new FeedbackResponseStatistic(jan3, 6, 11);
        frsDb.createEntity(FeedbackResponseStatisticAttributes.valueOf(frs1));
        frsDb.createEntity(FeedbackResponseStatisticAttributes.valueOf(frs2));
        frsDb.createEntity(FeedbackResponseStatisticAttributes.valueOf(frs3));
    }

    @Test
    public void testGetFeedbackResponseStatistics() {
        assertEquals(frsLogic.getFeedbackResponseStatistics(jan1, end).size(), 3);
        assertEquals(frsLogic.getFeedbackResponseStatistics(jan2, jan3).size(), 1);
        assertEquals(frsLogic.getFeedbackResponseStatistics(jan4, jan5).size(), 0);
    }

    @Test
    public void testSetFeedbackResponseStatistic_statisticDoesNotExist_shouldCreateCount()
            throws InvalidParametersException, EntityAlreadyExistsException {
        frsLogic.setFeedbackResponseStatistic(jan4, 14);
        FeedbackResponseStatisticAttributes frsa = frsLogic.getFeedbackResponseStatistics(jan4, jan5).get(0);
        assertEquals(3, frsa.getCount());
    }

    @Test
    public void testSetFeedbackResponseStatistic_statisticAlreadyExists_shouldUpdateCount()
            throws InvalidParametersException, EntityAlreadyExistsException {
        frsLogic.setFeedbackResponseStatistic(jan1, 3);
        FeedbackResponseStatisticAttributes frsa = frsLogic.getFeedbackResponseStatistics(jan1, end).get(0);
        assertEquals(3, frsa.getCount());
    }
}
