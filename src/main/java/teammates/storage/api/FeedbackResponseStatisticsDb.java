package teammates.storage.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import teammates.common.datatransfer.AttributesDeletionQuery;
import teammates.common.datatransfer.attributes.FeedbackResponseStatisticAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Assumption;
import teammates.common.util.Const;
import teammates.common.util.TimeHelper;
import teammates.storage.entity.FeedbackResponse;
import teammates.storage.entity.FeedbackResponseStatistic;

/**
 * Handles CRUD operations for feedback response statistics.
 *
 * @see FeedbackResponseStatistic
 * @see FeedbackResponseStatisticAttributes
 */
public class FeedbackResponseStatisticsDb extends EntitiesDb<FeedbackResponseStatistic, FeedbackResponseStatisticAttributes> {

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

    public boolean hasFeedbackResponseStatistic(Instant time) {
        return getFeedbackResponseStatistic(time) != null;
    }

    public boolean isFeedbackResponseStatisticCountOne(Instant time) {
        return getFeedbackResponseStatistic(time).getCount() == 1;
    }

    public FeedbackResponseStatisticAttributes incrementFeedbackResponseStatistic(Instant time) {
        FeedbackResponseStatistic frs = getFeedbackResponseStatistic(time);
        frs.incrementCount();
        saveEntity(frs);
        return makeAttributes(frs);
    }

    public void decrementFeedbackResponseStatistic(Instant time) {
        FeedbackResponseStatistic frs = getFeedbackResponseStatistic(time);
        frs.decrementCount();
        saveEntity(frs);
    }

    public FeedbackResponseStatistic getFeedbackResponseStatistic(Instant time) {
        return load().id(FeedbackResponseStatistic.generateId(time)).now();
    }
    /**
     *
     * Deletes a feedback response statistic.
     */
    public void deleteFeedbackResponseStatistic(Instant time) {
        Assumption.assertNotNull(time);
        long statisticId = FeedbackResponseStatistic.generateId(time);

        deleteEntity(Key.create(FeedbackResponseStatistic.class, statisticId));
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
