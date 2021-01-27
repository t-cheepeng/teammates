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

/**
 * Handles operations related to feedback response statistics.
 *
 * @see FeedbackResponseStatsticAttributes
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
     * Creates a feedback response statistic with count 1.
     * If the feedback response statistic already exists, increment the count instead.
     *
     * @return created feedback response statistic
     * @throws InvalidParametersException if the feedback response statistic is not valid
     * @throws EntityAlreadyExistsException if the feedback response statistic already exists
     */
    public FeedbackResponseStatisticAttributes createFeedbackResponseStatistic(Instant time)
            throws InvalidParametersException, EntityAlreadyExistsException {
        if (frsDb.hasFeedbackResponseStatistic(time)) { // already exists, increment
            return frsDb.incrementFeedbackResponseStatistic(time);
        } else {                                        // does not exist, create
            FeedbackResponseStatisticAttributes frsa = new FeedbackResponseStatisticAttributes(time, 1);
            return frsDb.createEntity(frsa);
        }
    }

    /**
     * Decrements the feedback response statistic using the timestamp.
     * If the feedback response statistic has only one count, delete the entity from the database.
     */
    public void deleteFeedbackResponseStatistic(Instant time) {
        if (frsDb.isFeedbackResponseStatisticCountOne(time)) { // count is going to be zero, delete
            frsDb.deleteFeedbackResponseStatistic(time);
        } else { // decrement
            frsDb.decrementFeedbackResponseStatistic(time);
        }
    }

    public static FeedbackResponseStatisticsLogic inst() {
        return instance;
    }
}
