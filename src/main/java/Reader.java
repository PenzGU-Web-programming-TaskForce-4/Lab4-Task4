import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Абстрактный класс занимающийся переработкой вводимых пользовательских строк в какие-либо объекты
 * @param <T> Выходной тип
 */
public abstract class Reader<T> {

    final private String invitationMessage;
    final private String regex;
    final private String regexTemplateMessage;

    /**
     * Основной конструктор принимающий все значения для полей абстрактного Reader'а
     * @param invitationMessage сообщение оглавляющее сегмент попыток ввода
     * @param regex паттерн, которому должно будет соответствовать пользовательский ввод, определяется в наследных классах, где и будет определён парсинг
     * @param regexTemplateMessage сообщение, показывающее пользователю паттерн чтобы он ему начал соответствовать
     */
    public Reader(String invitationMessage, String regex, String regexTemplateMessage) {
        this.invitationMessage = invitationMessage;
        this.regex = regex;
        this.regexTemplateMessage = regexTemplateMessage;
    }

    /**
     * Парсит пользовательскую строку в объект
     * @param string входящая строка полученная из пользовательского ввода
     * @return распарсенный объект
     */
    protected abstract T parse(String string);

    /**
     * Читает пользовательский ввод и парсит его с помощью абстрактного метода Reader::parse
     * @see #parse
     * @param scanner предоставляет доступ к пользовательскому вводу
     * @return распарсенный объект
     */
    final public T read(Scanner scanner) {
        System.out.println(invitationMessage);
        String line = scanner.nextLine();
        while (!Pattern.matches(regex, line)) {
            System.out.printf(regexTemplateMessage, regex);
            line = scanner.nextLine();
        }
        return parse(line);
    }
}
