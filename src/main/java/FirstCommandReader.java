/**
 * Парсер первой команды юзера
 */
public class FirstCommandReader extends Reader<FirstCommand> {

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
