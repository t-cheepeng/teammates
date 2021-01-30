package teammates.storage.entity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Translate;
import com.googlecode.objectify.annotation.Unindex;

/**
 * Represents a feedback response statistic.
 */
@Entity
@Index
public class FeedbackResponseStatistic extends BaseEntity {

    /**
     * The unique id of the entity.
     *
     * @see #generateId(Instant)
     */
    @Id
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private long id;

    @Translate(InstantTranslatorFactory.class)
    private Instant time;

    @Unindex
    private int count;

    @Unindex
    private int totalCount;

    @SuppressWarnings("unused")
    private FeedbackResponseStatistic() {
        // required by Objectify
    }

    public FeedbackResponseStatistic(Instant time, int count, int totalCount) {
        setTime(time);
        setCount(count);
        this.id = generateId(time);
        this.totalCount = totalCount;
    }

    /**
     * Generates an unique ID for the feedback response statistic.
     * Rounds down to the nearest minute.
     */
    public static long generateId(Instant time) {
        return time.truncatedTo(ChronoUnit.MINUTES).toEpochMilli();
    }

    public void setTime(Instant time) {
        this.time = time.truncatedTo(ChronoUnit.MINUTES);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Instant getTime() {
        return this.time;
    }

    public int getCount() {
        return this.count;
    }

    public int getTotalCount() {
        return this.totalCount;
    }
}
