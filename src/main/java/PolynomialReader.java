import java.util.Arrays;

/**
 * Reader Многочленов
 */
public class PolynomialReader extends Reader<Polynomial> {

    /**
     * Конструктор Reader'а многочленов
     * @param invitation сообщение оглавляющее сегмент попыток ввода
     * @param regexTemplateMessage сообщение, показывающее пользователю паттерн чтобы он ему начал соответствовать
     */
    public PolynomialReader(String invitation, String regexTemplateMessage) {
        super(invitation, "(-?\\d+)(\\s-?\\d+)*", regexTemplateMessage);
    }

    /**
     * Получает из введённой строки числа(коэффициенты многочлена) и создаёт из них объект Многочлена
     * @param string входящая строка полученная из пользовательского ввода
     * @return Многочлен
     */
    @Override
    protected Polynomial parse(String string) {
        var coefficients = Arrays.stream(string.split("\\s")).map(Integer::valueOf).toList();
        return new Polynomial(coefficients);
    }
}
