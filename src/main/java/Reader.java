import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class Reader<T> {

    final private String invitation;
    final private String regex;

    public Reader(String invitation, String regex) {
        this.invitation = invitation;
        this.regex = regex;
    }

    protected abstract T parse(String string);

    final public T read(Scanner scanner) {
        System.out.println(invitation);
        String line = scanner.nextLine();
        while (!Pattern.matches(regex, line)) {
            System.out.printf("Your input does not match the pattern %s, try again%n", regex);
            line = scanner.nextLine();
        }

        return parse(line);
    }
}
