package calculator;

import calculator.input.DateIntervalRecordScanner;
import calculator.output.EligibilityReportPrinter;
import calculator.util.DateInterval;
import calculator.util.DateIntervalComparator;

import java.time.LocalDate;
import java.util.List;

/**
 * To be eligible for settled status, you usually need to have lived in the UK, the Channel Islands or the Isle of Man
 * for at least 6 months in any 12 month period for 5 years in a row.
 */
public class SettledStatusEligibilityCalculator {
    private static final String MY_ENTRY_DATES_FILE_PATH = "C:\\Users\\Alex\\IdeaProjects\\EligibilityChecker\\src\\main\\resources\\BrexitRun2020.csv";
    private static final int EU_CITIZEN_YEARS_REQUIRED = 5;
    private static final int EU_CITIZEN_MONTHS_PER_YEAR_REQUIRED = 6;
    private static final int YEAR_LENGTH_IN_DAYS = 365;

    private DateIntervalRecordScanner dateIntervalRecordScanner;
    private EligibilityReportPrinter reportPrinter;
    private int yearsRequired;
    private int daysRequiredPerYear;
    private boolean eligible;
    private int daysLivedContinuously;
    private int additionalDaysRequired;
    private List<DateInterval> intervals;

    public static SettledStatusEligibilityCalculator euCalculator(String entryDatesFilePath) {
        return new SettledStatusEligibilityCalculator(entryDatesFilePath, EU_CITIZEN_YEARS_REQUIRED, EU_CITIZEN_MONTHS_PER_YEAR_REQUIRED);
    }

    public SettledStatusEligibilityCalculator(String entryDatesFilePath, int yearsRequired, int monthsPerYearRequired) {
        this.dateIntervalRecordScanner = new DateIntervalRecordScanner(entryDatesFilePath);
        this.yearsRequired = yearsRequired;
        this.daysRequiredPerYear = monthsToDays(monthsPerYearRequired);
        this.additionalDaysRequired = YEAR_LENGTH_IN_DAYS * yearsRequired;

        computeEligibility();
        reportPrinter = new EligibilityReportPrinter.Builder(daysLivedContinuously)
                .additionalDaysRequired(additionalDaysRequired)
                .build();
    }

    private int monthsToDays(int numberOfMonths) {
        int daysInShortMonth = 30;
        int daysInLongMonth = 31;
        int numberOfShortMonths = numberOfMonths / 2;
        int numberOfLongMonths = numberOfMonths - numberOfShortMonths;

        return numberOfShortMonths * daysInShortMonth + numberOfLongMonths * daysInLongMonth;
    }

    private void computeEligibility() {
        intervals = dateIntervalRecordScanner.readAllIntervals();
        intervals.sort(new DateIntervalComparator());
        LocalDate cutoffDate = intervals.get(intervals.size() - 1).getEnd().minusYears(yearsRequired);
        LocalDate lastInputDate = intervals.get(intervals.size() - 1).getEnd();
        DateInterval intervalToCheck = new DateInterval(lastInputDate.minusYears(1), lastInputDate);

        eligible = true;
        while (intervalToCheck.getStart().isAfter(cutoffDate)) {
            int daysLivedInInterval = computeDaysLivedInInterval(intervalToCheck);
            if (daysLivedInInterval < daysRequiredPerYear) {
                eligible = false;
                break;
            }
            if (daysLivedContinuously == 0) {
                daysLivedContinuously = daysLivedInInterval;
                additionalDaysRequired -= daysLivedInInterval;
            } else {
                daysLivedContinuously++;
                additionalDaysRequired--;
            }
            intervalToCheck.shiftIntervalByDays(-1);
        }
    }

    private int computeDaysLivedInInterval(DateInterval interval) {
        int amountInDays = 0;
        for (LocalDate date = interval.getStart(); date.isBefore(interval.getEnd()); date = date.plusDays(1)) {
            if (isDateSpentInUK(date)) {
                amountInDays++;
            }
        }

        return amountInDays;
    }

    private boolean isDateSpentInUK(LocalDate date) {
        for (DateInterval interval : intervals) {
            if (interval.containsDate(date)) {
                return true;
            }
        }

        return false;
    }

    public void printResult() {
        reportPrinter.printReport();
    }

    public static void main(String[] args) {
        try {
            String entryDatesFilePath = args.length > 0 ? args[0] : MY_ENTRY_DATES_FILE_PATH;
            SettledStatusEligibilityCalculator myCalculator = SettledStatusEligibilityCalculator.euCalculator(entryDatesFilePath);
            myCalculator.printResult();
        } catch (Exception e) {
            System.err.println("Could not read file.");
        }
    }
}
