import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.BinaryOperator;

/**
 * Main, ну что тут сказать
 */
public class Main {

    private static final String MAIN_INVITATION = "" +
            "Ларюшкин Сергей, Мишихин Владислав, Карамышев Глеб 21ВП1 - Web-программирование - Лабораторная работа №4, Вариант 4\n" +
            "Краткое описание: Вы введёте в начале либо число оперируемых многочленов, либо команду 'cancel', а затем на каждой новой строке будет писать многочлен, а затем операцию над ним (либо '+', либо '*'), оканчивается ввод многочленом";
    private static final String ERR_TEMPLATE_MESSAGE = "Что-то пошло не так при работе с output.results.txt%nОшибка: %s&n";
    private static final String OUTPUT_RESULTS_FILE = "output.results.txt";
    private static final String OPERATION_INPUT_INVITATION_MESSAGE = "Введите операцию, которую хотите произвести, один символ, либо '*', либо '+'";
    private static final String POLYNOMIAL_INPUT_INVITATION_MESSAGE = "Введите коэффициенты,\nНапример: 1 -2 0 4, эти числа соответствуют такому выражению: X^3 + (-2)*X^2 + 4";
    private static final String INPUT_REGEX_TEMPLATE_MESSAGE = "Ваш ввод не соответствует паттерну %s, попробуйте снова%n";
    private static final String OUTPUT_YOUR_POLYNOMIALS_MESSAGE = "Ваши многочлены:\n";
    private static final String OUTPUT_ARG_PREFIX = ">> ";
    private static final char END_LINE = '\n';
    private static final String OUTPUT_CALCULATION_RESULT_PREFIX = "=> ";
    private static final String OUTPUT_CALCULATION_RESULTS_MESSAGE = "Результаты вычислений:\n";

    /**
     * Точка входа Main.main, ну что тут сказать
     *
     * @param commandLineArgs ничего не принимаем
     */
    public static void main(String[] commandLineArgs) {
        var polynomialReader = new PolynomialReader(POLYNOMIAL_INPUT_INVITATION_MESSAGE, INPUT_REGEX_TEMPLATE_MESSAGE);
        var operationReader = new PolynomialOperationReader(OPERATION_INPUT_INVITATION_MESSAGE, INPUT_REGEX_TEMPLATE_MESSAGE);
        var firstCommandReader = new FirstCommandReader(MAIN_INVITATION, INPUT_REGEX_TEMPLATE_MESSAGE);

        var args = readArgs(polynomialReader, operationReader, firstCommandReader);
        var calculationsIterator = new Polynomial.CalculationsIterator(args.polynomials, args.operations);

        writeResults(args.polynomials, calculationsIterator);
    }

    /**
     * Читаем ввод
     *
     * @param polynomialReader читает многочлены
     * @param operationReader читает операции
     * @param firstCommandReader читает первую команду
     * @return Аргументы - многочлены и операции
     */
    private static Args readArgs(PolynomialReader polynomialReader,
                                 PolynomialOperationReader operationReader,
                                 FirstCommandReader firstCommandReader) {
        Polynomial[] polynomials;
        BinaryOperator<Polynomial>[] operations;
        try (var scanner = new Scanner(System.in)) {
            var firstCommand = firstCommandReader.read(scanner);

            if (firstCommand instanceof FirstCommand.Cancel) System.exit(0);
            var polynomialsCount = ((FirstCommand.PolynomialsCount) firstCommand).getCount();
            if (polynomialsCount == 0) System.exit(0);

            polynomials = new Polynomial[polynomialsCount];
            //noinspection unchecked
            operations = new BinaryOperator[polynomialsCount - 1];

            for (int i = 0; i < polynomialsCount - 1; i++) {
                polynomials[i] = polynomialReader.read(scanner);
                operations[i] = operationReader.read(scanner);
            }
            polynomials[polynomialsCount - 1] = polynomialReader.read(scanner);
        }
        return new Args(List.of(polynomials), List.of(operations));
    }

    /**
     * Пишем результаты
     *
     * @param polynomials введённые многочлены
     * @param calculationsIterator итератор производимых операций
     */
    private static void writeResults(List<Polynomial> polynomials, Iterator<Polynomial> calculationsIterator) {
        try (var outWriter = new BufferedWriter(new FileWriter(OUTPUT_RESULTS_FILE))) {
            outWriter.append(OUTPUT_YOUR_POLYNOMIALS_MESSAGE);
            for (var polynomial : polynomials) {
                outWriter.append(OUTPUT_ARG_PREFIX).append(polynomial.toString()).append(END_LINE);
            }
            outWriter.append(OUTPUT_CALCULATION_RESULTS_MESSAGE);
            calculationsIterator.forEachRemaining(polynomial -> {
                try {
                    outWriter.append(OUTPUT_CALCULATION_RESULT_PREFIX).append(polynomial.toString()).append(END_LINE);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            System.err.printf(ERR_TEMPLATE_MESSAGE, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                System.err.printf(ERR_TEMPLATE_MESSAGE, e.getMessage());
            } else {
                throw e;
            }
        }
    }

    /**
     * Вводимые аргументы, вынесены чтобы можно было вернуть их из функции ввода наружу
     * @param polynomials
     * @param operations
     */
    private record Args(
            List<Polynomial> polynomials,
            List<BinaryOperator<Polynomial>> operations
    ) {
    }
}

/**
 * Первая команда пользователя
 */
sealed class FirstCommand {

    /**
     * Пользователь ввёл количество многочленов
     */
    static final class PolynomialsCount extends FirstCommand {

        private final int count;

        PolynomialsCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Пользователь пожелал выйти
     */
    static final class Cancel extends FirstCommand {
    }
}

/**
 * Парсер первой команды юзера
 */
class FirstCommandReader extends Reader<FirstCommand> {

    /**
     * Основной конструктор принимающий все значения для полей абстрактного Reader'а
     *
     * @param invitationMessage    сообщение оглавляющее сегмент попыток ввода
     * @param regexTemplateMessage сообщение, показывающее пользователю паттерн чтобы он ему начал соответствовать
     */
    public FirstCommandReader(String invitationMessage, String regexTemplateMessage) {
        super(invitationMessage, "(cancel)|(\\d+)", regexTemplateMessage);
    }

    /**
     * Распознаёт во введённой команде либо операцию отмены(cancel), либо число
     *
     * @param string входящая строка полученная из пользовательского ввода
     * @return первую команду пользователя в программе
     */
    @Override
    protected FirstCommand parse(String string) {
        return string.equals("cancel") ?
                new FirstCommand.Cancel() :
                new FirstCommand.PolynomialsCount(Integer.parseInt(string));
    }
}
