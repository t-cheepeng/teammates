package teammates.logic.core;

import java.time.Instant;
import java.util.List;

import teammates.common.datatransfer.attributes.FeedbackResponseStatisticAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.storage.api.FeedbackResponseStatisticsDb;

/**
 * Handles operations related to feedback response statistics.
 *
 * @see FeedbackResponseStatisticAttributes
 * @see FeedbackResponseStatisticsDb
 */
public final class FeedbackResponseStatisticsLogic {

    private static FeedbackResponseStatisticsLogic instance = new FeedbackResponseStatisticsLogic();
    private static final FeedbackResponseStatisticsDb frsDb = new FeedbackResponseStatisticsDb();

    private FeedbackResponseStatisticsLogic() {
        // prevent initialization
    }

    /**
     * Gets the list of feedback response statistics.
     */
    public List<FeedbackResponseStatisticAttributes> getFeedbackResponseStatistics(Instant start, Instant end) {
        return frsDb.getFeedbackResponseStatistics(start, end);
    }

    /**
     * Creates a feedback response statistic with given total count.
     * If the feedback response statistic already exists, update the count.
     *
     * @return created feedback response statistic
     * @throws InvalidParametersException   if the feedback response statistic is not valid
     * @throws EntityAlreadyExistsException if the feedback response statistic already exists
     */
    public FeedbackResponseStatisticAttributes setFeedbackResponseStatistic(Instant time, int totalCount)
            throws InvalidParametersException, EntityAlreadyExistsException {
        try {
            int previousFrsTotalCount = frsDb.getPreviousFeedbackResponseStatisticTotalCount(time);
            int newCount = totalCount - previousFrsTotalCount;
            return setFeedbackResponseStatistic(time, newCount, totalCount);
        } catch (EntityDoesNotExistException e) {
            return setFeedbackResponseStatistic(time, totalCount, totalCount);
        }
    }

    private FeedbackResponseStatisticAttributes setFeedbackResponseStatistic(Instant time, int count, int totalCount)
            throws InvalidParametersException, EntityAlreadyExistsException {
        if (frsDb.hasFeedbackResponseStatistic(time)) { // already exists, increment
            int frsTotalCount = frsDb.getFeedbackResponseStatisticTotalCount(time);
            if (frsTotalCount == totalCount) {
                return FeedbackResponseStatisticAttributes.valueOf(frsDb.getFeedbackResponseStatistic(time));
            }
            return frsDb.setFeedbackResponseStatisticCount(time, count, totalCount);
        } else { // does not exist, create
            FeedbackResponseStatisticAttributes frsa = new FeedbackResponseStatisticAttributes(time, count, totalCount);
            return frsDb.createEntity(frsa);
        }
    }

    public static FeedbackResponseStatisticsLogic inst() {
        return instance;
    }
}
