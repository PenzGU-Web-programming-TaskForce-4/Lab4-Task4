import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main, ну что тут сказать
 */
public class Main {

    private static final String ERR_TEMPLATE_MESSAGE = "Что-то пошло не так при работе с output.results.txt%nОшибка: %s&nРезультат: %s%n";
    private static final String OUTPUT_RESULTS_FILE = "output.results.txt";
    private static final String INVITATION_MESSAGE = "Пожалуйста, введите коэффициенты,\nНапример: 1 -2 0 4, эти числа соответствуют такому выражению: X^3 + (-2)*X^2 + 4";
    private static final String READING_FIRST_POLYNOMIAL_MESSAGE = "Читаем первый многочлен";
    private static final String READING_SECOND_POLYNOMIAL_MESSAGE = "Читаем второй многочлен";
    private static final String RESULT_TEMPLATE_MESSAGE = "Результат: %s%n";
    private static final String REGEX_TEMPLATE_MESSAGE = "Your input does not match the pattern %s, try again%n";

    /**
     * Точка входа Main.main, ну что тут сказать
     *
     * @param args ничего не принимаем
     */
    public static void main(String[] args) {
        var polynomialReader = new PolynomialReader(INVITATION_MESSAGE, REGEX_TEMPLATE_MESSAGE);

        Polynomial a, b;
        try (var scanner = new Scanner(System.in)) {
            System.out.println(READING_FIRST_POLYNOMIAL_MESSAGE);
            a = polynomialReader.read(scanner);
            System.out.println(READING_SECOND_POLYNOMIAL_MESSAGE);
            b = polynomialReader.read(scanner);
        }

        var result = a.multiply(b);

        try (var outWriter = new BufferedWriter(new FileWriter(OUTPUT_RESULTS_FILE))) {
            outWriter.append(RESULT_TEMPLATE_MESSAGE.formatted(result.toString()));
        } catch (IOException exception) {
            System.err.printf(ERR_TEMPLATE_MESSAGE, exception.getMessage(), result);
        }
    }
}
