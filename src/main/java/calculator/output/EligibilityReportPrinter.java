package calculator.output;

import java.io.PrintStream;
import java.util.Objects;

public class EligibilityReportPrinter {
    private final int daysLivedContinuously;
    private final int additionalDaysRequired;
    private final PrintStream printStream;

    public static class Builder {
        private final int daysLivedContinuously;

        private int additionalDaysRequired = 0;
        private PrintStream printStream = System.out;

        public Builder(int daysLivedContinuously) {
            if (daysLivedContinuously < 0) {
                throw new IllegalArgumentException(String.format("Days lived continuously must be non-negative; received %d.", daysLivedContinuously));
            }
            this.daysLivedContinuously = daysLivedContinuously;
        }


        public Builder additionalDaysRequired(int val) {
            if (val < 0) {
                throw new IllegalArgumentException(String.format("Additional days required must be non-negative; received %d.", val));
            }
            additionalDaysRequired = val; return this;
        }

        public Builder printStream(PrintStream val) {
            printStream = Objects.requireNonNull(val, "Print stream must not be null");
            return this;
        }


        public EligibilityReportPrinter build() {
            return new EligibilityReportPrinter(daysLivedContinuously, additionalDaysRequired, printStream);
        }
    }

    private EligibilityReportPrinter(int daysLivedContinuously, int additionalDaysRequired, PrintStream printStream) {
        this.daysLivedContinuously = daysLivedContinuously;
        this.additionalDaysRequired = additionalDaysRequired;
        this.printStream = printStream;
    }

    public void printReport() {
        if (additionalDaysRequired == 0) {
            printStream.println("Congratulations, you are eligible!");
        } else {
            printStream.println("Sorry, you are not yet eligible.");
            printStream.println(String.format("You still need %d days.", additionalDaysRequired));
        }
        printStream.println(String.format("You have lived %d days continuously in the UK.", daysLivedContinuously));
    }
}
