package calculator.util;

import java.util.Comparator;

public class DateIntervalComparator implements Comparator<DateInterval> {
    @Override
    public int compare(DateInterval o1, DateInterval o2) {
        int startComparison = o1.getStart().compareTo(o2.getStart());

        return startComparison == 0 ? o1.getEnd().compareTo(o2.getEnd()) : startComparison;
    }
}
