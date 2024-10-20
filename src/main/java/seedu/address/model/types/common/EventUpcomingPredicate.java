package seedu.address.model.types.common;

import java.time.LocalDateTime;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.types.event.Event;

/**
 * Tests that a {@code Event}'s {@code StartDate} happens on or before a given Date.
 */
public class EventUpcomingPredicate implements Predicate<Event> {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    /**
     * Constructs an EventUpcomingPredicate with a positive or negative number of days
     * This constructor is used to create a filter for the past N or next N days of events
     * @param range the number of days in the future/past.
     */
    public EventUpcomingPredicate(Integer range) {
        if (range >= 0) {
            startDate = DateTimeUtil.getCurrentDateTime();
            endDate = DateTimeUtil.getCurrentDateTime().plusDays(range);
        } else {
            startDate = DateTimeUtil.getCurrentDateTime().minusDays(range);
            endDate = DateTimeUtil.getCurrentDateTime();
        }
    }

    /**
     * Constructs an UpcomingEventCommand with a specific date
     * This constructor is used to create a filter for events that happen on that date
     * @param dateTime the number of days in the future/past.
     */
    public EventUpcomingPredicate(DateTime dateTime) {
        startDate = dateTime.toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
        endDate = dateTime.toLocalDateTime().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
    }

    @Override
    public boolean test(Event event) {
        LocalDateTime eventLocalDateTime = event.getStartTime().toLocalDateTime();
        return eventLocalDateTime.isAfter(startDate) && eventLocalDateTime.isBefore(endDate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EventNameContainsKeywordsPredicate)) {
            return false;
        }

        EventUpcomingPredicate otherEventUpcomingPredicate =
                (EventUpcomingPredicate) other;
        return startDate.equals(otherEventUpcomingPredicate.startDate)
                && endDate.equals(otherEventUpcomingPredicate.endDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("start", startDate).add("end", endDate).toString();
    }
}
