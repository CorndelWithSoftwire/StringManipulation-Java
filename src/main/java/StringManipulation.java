import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringManipulation {
    private static final String INPUT = "Publication Date,Title,Authors\n" +
            "29/07/1954,Lord of the Rings,John Ronald Reuel Tolkien\n" +
            "01/08/1996,A Game of Thrones,George Raymond Martin\n" +
            "21/06/2003,Harry Potter and the Order of the Phoenix,Joanne Rowling";

    private static final String EXPECTED_OUTPUT = "| Pub Date    |                         Title | Authors               |\n" +
            "|=====================================================================|\n" +
            "| 29 Jul 1954 |             Lord of the Rings | John Ronald Reuel ... |\n" +
            "| 01 Aug 1996 |             A Game of Thrones | George Raymond Martin |\n" +
            "| 21 Jun 2003 | Harry Potter and the Order... |        Joanne Rowling |\n";

    private static int TITLE_COLUMN_WIDTH = 29;
    private static int AUTHOR_COLUMN_WIDTH = 21;
    private static int PUB_DATE_COLUMN_WIDTH = 11;

    private static final String TABLE_ROW_PATTERN = "| {0} | {1} | {2} |\n";
    private static DateTimeFormatter INPUT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static DateTimeFormatter OUTPUT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public static void main(String[] args) {
        String output = convertToTable(INPUT);

        System.out.println("Expected:");
        System.out.println(EXPECTED_OUTPUT);
        System.out.println();

        System.out.println("Actual:");
        System.out.println(output);
        System.out.println();

        if (output.equals(EXPECTED_OUTPUT)) {
            System.out.println("Output matched expected result");
        } else {
            System.out.println("Output didn't match expected result");
        }
    }

    private static String convertToTable(String input) {
        List<List<String>> rows =
                Arrays.stream(input.split("\n"))
                        .map(row -> Arrays.asList(row.split(",")))
                        .collect(Collectors.toList());

        String titleRow = MessageFormat.format(TABLE_ROW_PATTERN,
                StringUtils.rightPad("Pub Date", PUB_DATE_COLUMN_WIDTH),
                StringUtils.leftPad("Title", TITLE_COLUMN_WIDTH),
                StringUtils.rightPad("Authors", AUTHOR_COLUMN_WIDTH));

        StringBuilder builder = new StringBuilder();
        builder.append(titleRow)
                .append("|")
                .append(StringUtils.repeat("=", titleRow.length() - 3))
                .append("|\n");
        rows.stream().skip(1).forEach(row -> {
            builder.append(formatLine(LocalDate.parse(row.get(0), INPUT_DATETIME_FORMATTER), row.get(1), row.get(2)));
        });
        return builder.toString();
    }

    private static String formatLine(LocalDate pubDate, String title, String authors) {
        return MessageFormat.format(
                TABLE_ROW_PATTERN,
                pubDate.format(OUTPUT_DATETIME_FORMATTER),
                StringUtils.leftPad(limitLength(title, TITLE_COLUMN_WIDTH), TITLE_COLUMN_WIDTH),
                StringUtils.leftPad(limitLength(authors, AUTHOR_COLUMN_WIDTH), AUTHOR_COLUMN_WIDTH)
        );
    }

    private static String limitLength(String text, int length) {
        if (text.length() > length) {
            return text.substring(0, length - 3) + "...";
        }
        return text;
    }
}
