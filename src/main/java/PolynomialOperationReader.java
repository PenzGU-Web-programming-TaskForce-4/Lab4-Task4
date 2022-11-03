import java.util.function.BinaryOperator;

public class PolynomialOperationReader extends Reader<BinaryOperator<Polynomial>> {

    /**
     * Основной конструктор принимающий все значения для полей абстрактного Reader'а
     *
     * @param invitationMessage    сообщение оглавляющее сегмент попыток ввода
     * @param regexTemplateMessage сообщение, показывающее пользователю паттерн чтобы он ему начал соответствовать
     */
    public PolynomialOperationReader(String invitationMessage, String regexTemplateMessage) {
        super(invitationMessage, "[+*]", regexTemplateMessage);
    }

    /**
     * Распознаёт в строке либо операцию умножения(*), либо сложения(+)
     *
     * @param string входящая строка полученная из пользовательского ввода
     * @return операцию в виде BiFunction
     */
    @Override
    protected BinaryOperator<Polynomial> parse(String string) {
        return string.equals("*") ? Polynomial::multiply : Polynomial::plus;
    }
}
