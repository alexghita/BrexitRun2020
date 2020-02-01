package calculator.util;

import java.time.LocalDate;
import java.util.Objects;

public class DateInterval {
    private LocalDate start;
    private LocalDate end;

    public DateInterval(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public int getLengthInDays() {
        return start.until(end).getDays();
    }

    public boolean containsDate(LocalDate date) {
        return containsDateExceptBoundaries(date) || date.isEqual(start) || date.isEqual(end);
    }

    private boolean containsDateExceptBoundaries(LocalDate date) {
        return start.isBefore(date) && date.isBefore(end);
    }

    public void shiftIntervalByDays(long days) {
        start = start.plusDays(days);
        end = end.plusDays(days);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DateInterval)) {
            return false;
        }
        DateInterval interval = (DateInterval) obj;
        return Objects.equals(start, interval.start) && Objects.equals(end, interval.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
