package teammates.storage.api;

import java.time.Instant;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.FeedbackResponseStatisticAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.storage.entity.FeedbackResponseStatistic;
import teammates.test.BaseComponentTestCase;

/**
 * SUT: {@link FeedbackResponseStatisticsDb}.
 */
public class FeedbackResponseStatisticsDbTest extends BaseComponentTestCase {

    private FeedbackResponseStatisticsDb statisticsDb = new FeedbackResponseStatisticsDb();

    @BeforeMethod
    public void beforeMethod() throws Exception {
        for (int i = 1; i < 6; i++) {
            statisticsDb
                    .createEntity(FeedbackResponseStatisticAttributes
                            .valueOf(
                                    new FeedbackResponseStatistic(
                                            Instant.ofEpochSecond(i * 60),
                                            i,
                                            i)));
        }
    }

    @Test
    public void testGetFeedbackResponseStatistics() {
        ______TS("typical success case (sorted by time)");
        List<FeedbackResponseStatisticAttributes> statsList =
                statisticsDb.getFeedbackResponseStatistics(Instant.ofEpochSecond(60), Instant.ofEpochSecond(4 * 60));
        assertEquals(3, statsList.size());
        for (int i = 0; i < statsList.size(); i++) {
            assertEquals(i + 1, statsList.get(i).getCount());
        }
    }

    @Test
    public void testHasFeedbackResponseStatistic() {
        ______TS("statistic exist in db");
        statisticsDb.hasFeedbackResponseStatistic(Instant.ofEpochSecond(2 * 60));

        ______TS("statistic does not exist in db");
        statisticsDb.hasFeedbackResponseStatistic(Instant.ofEpochSecond(10 * 60));
    }

    @Test
    public void testSetFeedbackResponseStatisticCount() {
        ______TS("typical success case");
        FeedbackResponseStatisticAttributes frsa =
                statisticsDb.setFeedbackResponseStatisticCount(Instant.ofEpochSecond(3 * 60), 10, 10);
        assertEquals(10, frsa.getCount());
        assertEquals(10, frsa.getTotalCount());
        assertEquals(Instant.ofEpochSecond(3 * 60), frsa.getTime());
    }

    @Test
    public void testGetPreviousFeedbackResponseStatisticTotalCount() throws Exception {
        ______TS("typical success case");
        int totalCount = statisticsDb.getPreviousFeedbackResponseStatisticTotalCount(Instant.ofEpochSecond(5 * 60));
        assertEquals(4, totalCount);

        ______TS("get previous statistic of the first statistic");
        assertThrows(EntityDoesNotExistException.class, () -> {
            statisticsDb.getPreviousFeedbackResponseStatisticTotalCount(Instant.ofEpochSecond(60));
        });
    }
}
