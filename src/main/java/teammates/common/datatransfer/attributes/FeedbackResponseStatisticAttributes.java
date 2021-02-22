package teammates.common.datatransfer.attributes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import teammates.storage.entity.FeedbackResponseStatistic;

public class FeedbackResponseStatisticAttributes extends EntityAttributes<FeedbackResponseStatistic> {

    private Instant time;
    private int count;
    private int totalCount;

    public FeedbackResponseStatisticAttributes(Instant time, int count, int totalCount) {
        this.time = time.truncatedTo(ChronoUnit.MINUTES);
        this.count = count;
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

    public static FeedbackResponseStatisticAttributes valueOf(FeedbackResponseStatistic frs) {
        return new FeedbackResponseStatisticAttributes(frs.getTime(), frs.getCount(), frs.getTotalCount());
    }

    @Override
    public List<String> getInvalidityInfo() {
        return new ArrayList<>();
        // TODO: how to implement this?
    }

    @Override
    public boolean isValid() {
        return getInvalidityInfo().isEmpty();
    }

    @Override
    public FeedbackResponseStatistic toEntity() {
        return new FeedbackResponseStatistic(time, count, totalCount);
    }

    @Override
    public String toString() {
        return "FeedbackResponseStatisticAttributes [time="
                + time + ", count=" + count + "]";
    }

    @Override
    public int hashCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.time).append(this.count).append(this.totalCount);
        return stringBuilder.toString().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (this == other) {
            return true;
        } else if (this.getClass() == other.getClass()) {
            FeedbackResponseStatisticAttributes otherFrsa = (FeedbackResponseStatisticAttributes) other;
            return Objects.equals(this.time, otherFrsa.time)
                    && Objects.equals(this.count, otherFrsa.count);
        } else {
            return false;
        }
    }

    @Override
    public void sanitizeForSaving() {
        // nothing to sanitize before saving
    }
}
