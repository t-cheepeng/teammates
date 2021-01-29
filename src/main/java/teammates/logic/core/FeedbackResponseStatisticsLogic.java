package teammates.logic.core;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import teammates.common.datatransfer.AttributesDeletionQuery;
import teammates.common.datatransfer.CourseRoster;
import teammates.common.datatransfer.FeedbackParticipantType;
import teammates.common.datatransfer.UserRole;
import teammates.common.datatransfer.attributes.FeedbackQuestionAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseCommentAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseStatisticAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.storage.api.FeedbackResponseStatisticsDb;
import teammates.storage.entity.FeedbackResponseStatistic;

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
        List<FeedbackResponseStatisticAttributes> frsa = frsDb.getFeedbackResponseStatistics(start, end);
        return frsa;
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
            FeedbackResponseStatistic previousFrs = frsDb.getPreviousFeedbackResponseStatistic(time);
            int newCount = totalCount - previousFrs.getTotalCount();
            return setFeedbackResponseStatistic(time, newCount, totalCount);
        } catch (EntityDoesNotExistException e) {
            return setFeedbackResponseStatistic(time, totalCount, totalCount);
        }
    }

    private FeedbackResponseStatisticAttributes setFeedbackResponseStatistic(Instant time, int count, int totalCount)
            throws InvalidParametersException, EntityAlreadyExistsException {
        if (frsDb.hasFeedbackResponseStatistic(time)) { // already exists, increment
            FeedbackResponseStatistic frs = frsDb.getFeedbackResponseStatistic(time);
            if (frs.getTotalCount() == totalCount) {
                return FeedbackResponseStatisticAttributes.valueOf(frs);
            }
            return frsDb.setFeedbackResponseStatisticCount(time, count, totalCount);
        } else {                                        // does not exist, create
            FeedbackResponseStatisticAttributes frsa = new FeedbackResponseStatisticAttributes(time, count, totalCount);
            return frsDb.createEntity(frsa);
        }
    }

    public static FeedbackResponseStatisticsLogic inst() {
        return instance;
    }
}
