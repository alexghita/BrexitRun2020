package calculator.input;

import calculator.util.DateInterval;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateIntervalRecordScannerTest {
    private static final String TEST_FILE_PATH = "C:\\Users\\Alex\\IdeaProjects\\EligibilityChecker\\src\\test\\resources\\TestEntryDates.csv";
    private static final String TOO_MANY_VALUES_PER_LINE_TEST_FILE_PATH = "C:\\Users\\Alex\\IdeaProjects\\EligibilityChecker\\src\\test\\resources\\TooManyValuesPerLineTestEntryDates.csv";
    private static final String BADLY_FORMATTED_FILE_PATH = "C:\\Users\\Alex\\IdeaProjects\\EligibilityChecker\\src\\test\\resources\\BadlyFormattedDates.csv";
    private static final String EMPTY_FILE_PATH = "C:\\Users\\Alex\\IdeaProjects\\EligibilityChecker\\src\\test\\resources\\NoDates.csv";
    private static final String INVALID_DATE_PATTERN = "INVALID PATTERN";

    private DateIntervalRecordScanner scanner;

    @Test
    public void givenValidFilePath_whenScannerReadsAllDates_thenScannerReturnsCorrectListOfDates() {
        scanner = new DateIntervalRecordScanner(TEST_FILE_PATH);
        List<DateInterval> expectedResult = new ArrayList<>(List.of(
                new DateInterval(LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-10")),
                new DateInterval(LocalDate.parse("2010-02-01"), LocalDate.parse("2010-02-10")),
                new DateInterval(LocalDate.parse("2010-03-03"), LocalDate.parse("2011-03-03"))
        ));
        assertEquals(expectedResult, scanner.readAllIntervals());
    }

    @Test
    public void givenNonexistentFile_whenScannerReadsAllIntervals_thenScannerReturnsEmptyList() {
        scanner = new DateIntervalRecordScanner("");
        assertEquals(Collections.emptyList(), scanner.readAllIntervals());
    }

    @Test
    public void givenInvalidDatePattern_whenConstructingScanner_thenScannerThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new DateIntervalRecordScanner(TEST_FILE_PATH, INVALID_DATE_PATTERN));
    }

    @Test
    public void givenTooManyInputValuesPerLine_whenReadingAllIntervals_thenScannerThrowsException() {
        scanner = new DateIntervalRecordScanner(TOO_MANY_VALUES_PER_LINE_TEST_FILE_PATH);
        assertThrows(InputMismatchException.class, () -> scanner.readAllIntervals());
    }

    @Test
    public void givenInputFileWithBadlyFormattedDates_whenReadingAllIntervals_thenScannerThrowsException() {
        scanner = new DateIntervalRecordScanner(BADLY_FORMATTED_FILE_PATH);
        assertThrows(DateTimeParseException.class, () -> scanner.readAllIntervals());
    }

    @Test
    public void givenEmptyInputFile_whenReadingAllIntervals_thenScannerReturnsEmptyList() {
        scanner = new DateIntervalRecordScanner(EMPTY_FILE_PATH);
        assertEquals(Collections.emptyList(), scanner.readAllIntervals());
    }
}
