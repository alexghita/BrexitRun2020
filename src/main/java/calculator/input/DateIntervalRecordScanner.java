package calculator.input;

import calculator.util.DateInterval;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateIntervalRecordScanner {
    public static final String ISO_DATE_PATTERN = "yyyy-MM-dd";

    private final String inputFilePath;
    private final DateTimeFormatter formatter;

    public DateIntervalRecordScanner(String inputFilePath) {
        this(inputFilePath, ISO_DATE_PATTERN);
    }

    public DateIntervalRecordScanner(String inputFilePath, String datePattern) {
        this.inputFilePath = inputFilePath;
        this.formatter = DateTimeFormatter.ofPattern(datePattern);
    }

    public List<DateInterval> readAllIntervals() {
        List<DateInterval> intervals = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileInputStream(inputFilePath))) {
            while (scanner.hasNextLine()) {
                intervals.add(readNextInterval(scanner));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return intervals;
    }

    private DateInterval readNextInterval(Scanner scanner) {
        String line = scanner.nextLine();
        String[] values = line.split(",");

        if (values.length != 2) {
            throw new InputMismatchException(String.format("Expected exactly 2 comma-separated values on row %s, found %d",
                    line, values.length));
        }

        LocalDate start = LocalDate.parse(values[0], formatter);
        LocalDate end = LocalDate.parse(values[1], formatter);

        return new DateInterval(start, end);
    }
}
