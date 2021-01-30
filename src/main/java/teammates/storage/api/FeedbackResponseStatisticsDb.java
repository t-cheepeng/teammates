package teammates.storage.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;

import teammates.common.datatransfer.attributes.FeedbackResponseStatisticAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Assumption;
import teammates.storage.entity.FeedbackResponseStatistic;

/**
 * Handles CRUD operations for feedback response statistics.
 *
 * @see FeedbackResponseStatistic
 * @see FeedbackResponseStatisticAttributes
 */
public class FeedbackResponseStatisticsDb
        extends EntitiesDb<FeedbackResponseStatistic, FeedbackResponseStatisticAttributes> {

    /**
     * Gets all feedback response statistics between the start and end time.
     */
    public List<FeedbackResponseStatisticAttributes> getFeedbackResponseStatistics(Instant start, Instant end) {
        List<FeedbackResponseStatistic> feedbackResponseStatisticList = load()
                .filter("time >=", start)
                .filter("time <", end)
                .list();

        return makeAttributes(feedbackResponseStatisticList).stream()
                .sorted(Comparator.comparing(FeedbackResponseStatisticAttributes::getTime))
                .collect(Collectors.toList());
    }

    /**
     * Checks if there exist a feedback response statistic at time.
     */
    public boolean hasFeedbackResponseStatistic(Instant time) {
        return getFeedbackResponseStatistic(time) != null;
    }

    /**
     * Sets an existing feedback response statistic at time to count and totalCount.
     */
    public FeedbackResponseStatisticAttributes setFeedbackResponseStatisticCount(Instant time, int count, int totalCount) {
        FeedbackResponseStatistic frs = getFeedbackResponseStatistic(time);
        frs.setCount(count);
        frs.setTotalCount(totalCount);
        log.info(count + "," + totalCount);
        saveEntity(frs);
        return makeAttributes(frs);
    }

    /**
     * Gets a feedback response statistics at time.
     */
    public FeedbackResponseStatistic getFeedbackResponseStatistic(Instant time) {
        return load().id(FeedbackResponseStatistic.generateId(time)).now();
    }

    /**
     * Gets a feedback response statistic total count at time.
     */
    public int getFeedbackResponseStatisticTotalCount(Instant time) {
        return getFeedbackResponseStatistic(time).getTotalCount();
    }

    /**
     * Gets the feedback response statistic directly before the one at time.
     * If such a statistic does not exist, throws an error
     */
    public int getPreviousFeedbackResponseStatisticTotalCount(Instant time)
            throws EntityDoesNotExistException {
        FeedbackResponseStatistic frs = load()
                .filter("time <", time.truncatedTo(ChronoUnit.MINUTES))
                .order("-time")
                .first()
                .now();

        if (frs == null) {
            throw new EntityDoesNotExistException(ERROR_UPDATE_NON_EXISTENT);
        }

        log.info("prev=" + frs.getTime().toString() + "," + frs.getTotalCount());

        return frs.getTotalCount();
    }

    @Override
    LoadType<FeedbackResponseStatistic> load() {
        return ofy().load().type(FeedbackResponseStatistic.class);
    }

    @Override
    boolean hasExistingEntities(FeedbackResponseStatisticAttributes entityToCreate) {
        return !load()
                .filterKey(Key.create(FeedbackResponseStatisticAttributes.class,
                        FeedbackResponseStatistic.generateId(entityToCreate.getTime())))
                .list()
                .isEmpty();
    }

    @Override
    FeedbackResponseStatisticAttributes makeAttributes(FeedbackResponseStatistic entity) {
        Assumption.assertNotNull(entity);
        return FeedbackResponseStatisticAttributes.valueOf(entity);
    }
}
